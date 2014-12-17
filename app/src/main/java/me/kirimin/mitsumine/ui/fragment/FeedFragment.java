package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.FeedListener;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedListFilter;

public class FeedFragment extends AbstractFeedFragment implements FeedListener {

    public static FeedFragment newFragment(CATEGORY category, TYPE type) {
        FeedFragment fragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORY.class.getCanonicalName(), category);
        bundle.putSerializable(TYPE.class.getCanonicalName(), type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    void requestFeed() {
        showRefreshing();
        CATEGORY category = (CATEGORY) getArguments().getSerializable(CATEGORY.class.getCanonicalName());
        TYPE type = (TYPE) getArguments().getSerializable(TYPE.class.getCanonicalName());
        BookmarkFeedAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(getActivity()), this, category, type);
    }

    @Override
    public void onSuccess(List<Feed> feedList) {
        clearFeed();
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