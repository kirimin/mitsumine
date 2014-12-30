package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.BookmarkFeedJsonParser;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.util.FeedListFilter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
        BookmarkFeedAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(getActivity()), category, type)
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
                        setFeed(FeedListFilter.filter(feedList));
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