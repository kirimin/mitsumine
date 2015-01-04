package me.kirimin.mitsumine.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.network.api.BookmarkApiAccessor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegisterBookmarkFragment extends Fragment {

    private boolean isAlreadyBookmark;

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

        BookmarkApiAccessor.requestIsAlreadyBookmark(url, AccountDAO.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject object) {
                        changeBookmarkStatus(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        changeBookmarkStatus(false);
                    }
                });
        rootView.findViewById(R.id.RegisterBookmarkRegisterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                if (isAlreadyBookmark) {
                    BookmarkApiAccessor.requestDeleteBookmark(url, AccountDAO.get())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    changeBookmarkStatus(false);
                                    v.setEnabled(true);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    v.setEnabled(true);
                                }
                            });

                } else {
                    TextView comment = (TextView) rootView.findViewById(R.id.RegisterBookmarkCommentEditText);
                    BookmarkApiAccessor.requestAddBookmark(url, AccountDAO.get(), comment.getText().toString())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<JSONObject>() {
                                @Override
                                public void call(JSONObject object) {
                                    changeBookmarkStatus(true);
                                    v.setEnabled(true);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    v.setEnabled(true);
                                }
                            });
                }
            }
        });
        return rootView;
    }

    private void changeBookmarkStatus(boolean isAlreadyBookmark) {
        if (getView() == null) return;

        this.isAlreadyBookmark = isAlreadyBookmark;
        Button button = (Button) getView().findViewById(R.id.RegisterBookmarkRegisterButton);
        if (isAlreadyBookmark) {
            button.setText("削除");
        } else {
            button.setText("登録");
        }
    }
}
