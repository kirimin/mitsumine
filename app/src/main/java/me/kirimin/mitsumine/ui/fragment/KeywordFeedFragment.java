package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.db.NGWordDAO;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedFunc;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class KeywordFeedFragment extends AbstractFeedFragment {

    public static KeywordFeedFragment newFragment(String keyword) {
        KeywordFeedFragment fragment = new KeywordFeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    void requestFeed() {
        final List<Feed> readFeedList = FeedDAO.findAll();
        final List<String> ngWordList = NGWordDAO.findAll();
        BookmarkFeedAccessor
                .requestKeyword(RequestQueueSingleton.getRequestQueue(getActivity()), getArguments().getString("keyword"))
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
                        if (feedList.isEmpty()) {
                            Toast.makeText(getActivity(), R.string.keyword_search_toast_notfound, Toast.LENGTH_SHORT).show();
                            dismissRefreshing();
                            return;
                        }
                        setFeed(feedList);
                        dismissRefreshing();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
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