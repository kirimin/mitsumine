package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;

import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.db.NGWordDAO;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedFunc;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FeedFragment extends AbstractFeedFragment {

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
        final List<Feed> readFeedList = FeedDAO.findAll();
        final List<String> ngWordList = NGWordDAO.findAll();
        BookmarkFeedAccessor
                .requestCategory(RequestQueueSingleton.getRequestQueue(getActivity()), category, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(FeedFunc.mapToFeedList())
                .filter(FeedFunc.notContains(readFeedList))
                .filter(FeedFunc.notContainsWord(ngWordList))
                .toList()
                .subscribe(new Action1<List<Feed>>() {
                    @Override
                    public void call(List<Feed> feedList) {
                        clearFeed();
                        setFeed(feedList);
                        dismissRefreshing();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        dismissRefreshing();
                    }
                });
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