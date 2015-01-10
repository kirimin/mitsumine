package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.db.NGWordDAO;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.api.FeedApiAccessor;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedFunc;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
        final List<Feed> readFeedList = FeedDAO.findAll();
        final List<String> ngWordList = NGWordDAO.findAll();
        FeedApiAccessor
                .requestUserBookmark(RequestQueueSingleton.getRequestQueue(getActivity()), getArguments().getString("user"))
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
                            Toast.makeText(getActivity(), R.string.user_search_toast_notfound, Toast.LENGTH_SHORT).show();
                            dismissRefreshing();
                            return;
                        }
                        setFeed(feedList);
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