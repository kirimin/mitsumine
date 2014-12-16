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

public class KeywordFeedFragment extends AbstractFeedFragment implements FeedListener {

    public static KeywordFeedFragment newFragment(String keyword) {
        KeywordFeedFragment fragment = new KeywordFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    void requestFeed() {
        BookmarkFeedAccessor.requestKeyword(RequestQueueSingleton.getRequestQueue(getActivity()), this, getArguments().getString("keyword"));
        showRefreshing();
    }

    @Override
    public void onSuccess(List<Feed> feedList) {
        clearFeed();
        if (feedList.isEmpty()) {
            Toast.makeText(getActivity(), R.string.keyword_search_toast_notfound, Toast.LENGTH_SHORT).show();
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