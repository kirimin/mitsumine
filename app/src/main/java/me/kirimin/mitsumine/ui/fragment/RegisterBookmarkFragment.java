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

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.network.api.BookmarkApiAccessor;
import rx.android.events.OnTextChangeEvent;
import rx.android.observables.ViewObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegisterBookmarkFragment extends Fragment implements TagEditDialogFragment.OnOkClickListener {

    public static RegisterBookmarkFragment newFragment(String url) {
        RegisterBookmarkFragment fragment = new RegisterBookmarkFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @InjectView(R.id.card_view)
    View cardView;
    @InjectView(R.id.RegisterBookmarkCommentEditText)
    TextView commentTextView;
    @InjectView(R.id.RegisterBookmarkCommentCountTextView)
    TextView commentCountTextView;
    @InjectView(R.id.RegisterBookmarkTagListText)
    TextView tagListTextView;
    @InjectView(R.id.RegisterBookmarkDeleteButton)
    Button deleteButton;
    @InjectView(R.id.RegisterBookmarkRegisterButton)
    Button registerButton;
    @InjectView(R.id.RegisterBookmarkTagEditButton)
    Button tagEditButton;

    private boolean isAlreadyBookmark;
    private ArrayList<String> tags = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final String url = getArguments().getString("url");
        if (url == null) {
            throw new IllegalStateException("url is null");
        }
        final View rootView = inflater.inflate(R.layout.fragment_register_bookmark, container, false);
        ButterKnife.inject(this, rootView);
        cardView.setVisibility(View.INVISIBLE);
        BookmarkApiAccessor.requestBookmarkInfo(url, AccountDAO.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        changeBookmarkStatus(jsonObject != null);
                        cardView.setVisibility(View.VISIBLE);
                        if (jsonObject != null) {
                            try {
                                String comment = jsonObject.getString("comment");
                                JSONArray tagJsonArray = jsonObject.getJSONArray("tags");
                                tags.clear();
                                for (int i = 0; i < tagJsonArray.length(); i++) {
                                    tags.add(tagJsonArray.getString(i));
                                }
                                commentTextView.setText(comment);
                                tagListTextView.setText(TextUtils.join(", ", tags));
                            } catch (JSONException e) {
                            }
                        } else {
                            commentTextView.setText("");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        showToastIfExistsActivity(R.string.network_error);
                    }
                });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                registerButton.setEnabled(false);
                deleteButton.setEnabled(false);
                BookmarkApiAccessor.requestAddBookmark(url, AccountDAO.get(), commentTextView.getText().toString(), tags)
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
                                registerButton.setEnabled(true);
                                deleteButton.setEnabled(true);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                registerButton.setEnabled(true);
                                deleteButton.setEnabled(true);
                                showToastIfExistsActivity(R.string.network_error);
                            }
                        });
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                deleteButton.setEnabled(false);
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
        ViewObservable.text(commentTextView)
                .map(new Func1<OnTextChangeEvent, Integer>() {
                    @Override
                    public Integer call(OnTextChangeEvent onTextChangeEvent) {
                        return onTextChangeEvent.text.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        commentCountTextView.setText(getActivity().getString(R.string.register_bookmark_limit, integer));
                    }
                });
        tagEditButton.setOnClickListener(new View.OnClickListener() {
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
        tagListTextView.setText(TextUtils.join(", ", tags));
    }

    private void changeBookmarkStatus(boolean isAlreadyBookmark) {
        this.isAlreadyBookmark = isAlreadyBookmark;
        deleteButton.setEnabled(isAlreadyBookmark);
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
