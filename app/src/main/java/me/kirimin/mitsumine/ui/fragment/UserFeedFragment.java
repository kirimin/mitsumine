package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedJsonParser;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedListFilter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserFeedFragment extends AbstractFeedFragment {

    public static UserFeedFragment newFragment(String user) {
        UserFeedFragment fragment = new UserFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    void requestFeed() {
        BookmarkFeedAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(getActivity()), getArguments().getString("user"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JSONObject, List<Feed>>() {
                    @Override
                    public List<Feed> call(JSONObject jsonObject) {
                        try {
                            return BookmarkFeedJsonParser.parseResponse(jsonObject);
                        } catch (JSONException e) {
                            return null;
                        }
                    }
                })
                .subscribe(new Action1<List<Feed>>() {
                    @Override
                    public void call(List<Feed> feedList) {
                        clearFeed();
                        if (feedList.isEmpty()) {
                            Toast.makeText(getActivity(), R.string.user_search_toast_notfound, Toast.LENGTH_SHORT).show();
                            dismissRefreshing();
                            return;
                        }
                        setFeed(FeedListFilter.filter(feedList));
                        dismissRefreshing();
                    }
                });
        showRefreshing();
    }

    @Override
    boolean isUseReadLater() {
        return true;
    }

    @Override
    boolean isUseRead() {
        return true;
    }
}