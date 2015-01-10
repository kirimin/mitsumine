package me.kirimin.mitsumine.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.network.api.BookmarkApiAccessor;
import me.kirimin.mitsumine.util.EntryInfoFunc;
import rx.android.events.OnTextChangeEvent;
import rx.android.observables.ViewObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegisterBookmarkFragment extends Fragment implements TagEditDialogFragment.OnOkClickListener {

    private boolean isAlreadyBookmark;
    private ArrayList<String> tags = new ArrayList<>();

    public static RegisterBookmarkFragment newFragment(String url) {
        RegisterBookmarkFragment fragment = new RegisterBookmarkFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final String url = getArguments().getString("url");
        if (url == null) {
            throw new IllegalStateException("url is null");
        }
        final View rootView = inflater.inflate(R.layout.fragment_register_bookmark, container, false);
        final View cardView = rootView.findViewById(R.id.card_view);
        cardView.setVisibility(View.INVISIBLE);

        BookmarkApiAccessor.requestBookmarkInfo(url, AccountDAO.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        changeBookmarkStatus(jsonObject != null);
                        cardView.setVisibility(View.VISIBLE);
                        TextView commentText = (TextView) rootView.findViewById(R.id.RegisterBookmarkCommentEditText);
                        if (jsonObject != null) {
                            try {
                                String comment = jsonObject.getString("comment");
                                JSONArray tagJsonArray = jsonObject.getJSONArray("tags");
                                tags.clear();
                                for (int i = 0; i < tagJsonArray.length(); i++) {
                                    tags.add(tagJsonArray.getString(i));
                                }
                                commentText.setText(comment);
                                if (getView() != null) {
                                    TextView tagListText = (TextView) getView().findViewById(R.id.RegisterBookmarkTagListText);
                                    tagListText.setText(TextUtils.join(", ", tags));
                                }
                            } catch (JSONException e) {
                            }
                        } else {
                            commentText.setText("");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showToastIfExistsActivity(R.string.network_error);
                    }
                });

        rootView.findViewById(R.id.RegisterBookmarkRegisterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                final View deleteButton = rootView.findViewById(R.id.RegisterBookmarkDeleteButton);
                deleteButton.setEnabled(false);
                TextView comment = (TextView) rootView.findViewById(R.id.RegisterBookmarkCommentEditText);
                BookmarkApiAccessor.requestAddBookmark(url, AccountDAO.get(), comment.getText().toString(), tags)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<JSONObject>() {
                            @Override
                            public void call(JSONObject object) {
                                if (isAlreadyBookmark) {
                                    showToastIfExistsActivity(R.string.register_bookmark_edit_success);
                                } else {
                                    showToastIfExistsActivity(R.string.register_bookmark_register_success);
                                }
                                changeBookmarkStatus(true);
                                v.setEnabled(true);
                                deleteButton.setEnabled(false);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                v.setEnabled(true);
                                deleteButton.setEnabled(false);
                                showToastIfExistsActivity(R.string.network_error);
                            }
                        });
            }
        });
        rootView.findViewById(R.id.RegisterBookmarkDeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                BookmarkApiAccessor.requestDeleteBookmark(url, AccountDAO.get())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                changeBookmarkStatus(false);
                                showToastIfExistsActivity(R.string.register_bookmark_delete_success);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                v.setEnabled(true);
                                showToastIfExistsActivity(R.string.network_error);
                            }
                        });
            }
        });
        ViewObservable.text((TextView) rootView.findViewById(R.id.RegisterBookmarkCommentEditText))
                .map(new Func1<OnTextChangeEvent, Integer>() {
                    @Override
                    public Integer call(OnTextChangeEvent onTextChangeEvent) {
                        return onTextChangeEvent.text.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        TextView commentCountText = (TextView) rootView.findViewById(R.id.RegisterBookmarkCommentCountTextView);
                        commentCountText.setText(getActivity().getString(R.string.register_bookmark_limit, integer));
                    }
                });
        rootView.findViewById(R.id.RegisterBookmarkTagEditButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagEditDialogFragment.newInstance(tags, RegisterBookmarkFragment.this).show(getFragmentManager(), null);
            }
        });
        return rootView;
    }

    @Override
    public void onOkClick(ArrayList<String> tags) {
        this.tags = tags;
        if (getView() != null) {
            TextView tagListText = (TextView) getView().findViewById(R.id.RegisterBookmarkTagListText);
            tagListText.setText(TextUtils.join(", ", tags));
        }
    }

    private void changeBookmarkStatus(boolean isAlreadyBookmark) {
        if (getView() == null) return;

        this.isAlreadyBookmark = isAlreadyBookmark;
        getView().findViewById(R.id.RegisterBookmarkDeleteButton).setEnabled(isAlreadyBookmark);
        Button registerButton = (Button) getView().findViewById(R.id.RegisterBookmarkRegisterButton);
        if (isAlreadyBookmark) {
            registerButton.setText(getActivity().getString(R.string.register_bookmark_edit));
        } else {
            registerButton.setText(getActivity().getString(R.string.register_bookmark_resister));
        }
    }

    private void showToastIfExistsActivity(int messageResourceId) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), messageResourceId, Toast.LENGTH_SHORT).show();
        }
    }
}
