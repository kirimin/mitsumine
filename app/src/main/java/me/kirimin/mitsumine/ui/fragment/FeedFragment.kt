package me.kirimin.mitsumine.ui.fragment


import android.os.Bundle

import me.kirimin.mitsumine.db.FeedDAO
import me.kirimin.mitsumine.db.NGWordDAO
import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.network.api.FeedApi.CATEGORY
import me.kirimin.mitsumine.network.api.FeedApi.TYPE
import me.kirimin.mitsumine.network.RequestQueueSingleton
import me.kirimin.mitsumine.util.FeedFunc
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.Serializable

public class FeedFragment : AbstractFeedFragment() {

    var subscriptions = CompositeSubscription()

    override fun onDestroyView() {
        subscriptions.unsubscribe()
        super.onDestroyView()
    }

    override fun requestFeed() {
        showRefreshing()
        val category = getArguments().getSerializable(javaClass<CATEGORY>().getCanonicalName()) as CATEGORY
        val type = getArguments().getSerializable(javaClass<TYPE>().getCanonicalName()) as TYPE
        val readFeedList = FeedDAO.findAll()
        val ngWordList = NGWordDAO.findAll()
        subscriptions.add(FeedApi.requestCategory(RequestQueueSingleton.get(getActivity()), category, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { obj -> FeedFunc.jsonToObservable(obj) }
                .filter { feed -> !FeedFunc.contains(feed, readFeedList) && !FeedFunc.containsWord(feed, ngWordList) }
                .toList()
                .subscribe ({ feedList ->
                    clearFeed()
                    setFeed(feedList)
                    dismissRefreshing()
                }, { e -> dismissRefreshing() })
        )
    }

    override fun isUseReadLater(): Boolean {
        return true
    }

    override fun isUseRead(): Boolean {
        return true
    }

    companion object {

        public fun newFragment(category: CATEGORY, type: TYPE): FeedFragment {
            val fragment = FeedFragment()
            val bundle = Bundle()
            bundle.putSerializable(javaClass<CATEGORY>().getCanonicalName(), category as Serializable)
            bundle.putSerializable(javaClass<TYPE>().getCanonicalName(), type as Serializable)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}