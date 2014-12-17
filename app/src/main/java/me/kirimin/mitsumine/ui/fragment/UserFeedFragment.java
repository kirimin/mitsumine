package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.FeedListener;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedListFilter;

public class UserFeedFragment extends AbstractFeedFragment implements FeedListener {

    public static UserFeedFragment newFragment(String user) {
        UserFeedFragment fragment = new UserFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    void requestFeed() {
        BookmarkFeedAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(getActivity()), this, getArguments().getString("user"));
        showRefreshing();
    }

    @Override
    public void onSuccess(List<Feed> feedList) {
        clearFeed();
        if (feedList.isEmpty()) {
            Toast.makeText(getActivity(), R.string.user_search_toast_notfound, Toast.LENGTH_SHORT).show();
            dismissRefreshing();
            return;
        }
        setFeed(FeedListFilter.filter(feedList));
        dismissRefreshing();
    }

    @Override
    public void onError(String errorMessage) {
        dismissRefreshing();
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