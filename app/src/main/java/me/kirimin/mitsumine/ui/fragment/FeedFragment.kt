package me.kirimin.mitsumine.ui.fragment


import android.os.Bundle

import me.kirimin.mitsumine.db.FeedDAO
import me.kirimin.mitsumine.db.NGWordDAO
import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.network.api.FeedApi.CATEGORY
import me.kirimin.mitsumine.network.api.FeedApi.TYPE
import me.kirimin.mitsumine.util.FeedUtil
import rx.android.schedulers.AndroidSchedulers
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
        val category = arguments.getSerializable(CATEGORY::class.java.canonicalName) as CATEGORY
        val type = arguments.getSerializable(TYPE::class.java.canonicalName) as TYPE
        val readFeedList = FeedDAO.findAll()
        val ngWordList = NGWordDAO.findAll()
        subscriptions.add(FeedApi.requestCategory(category, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { feed -> !FeedUtil.contains(feed, readFeedList) && !FeedUtil.containsWord(feed, ngWordList) }
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
            bundle.putSerializable(CATEGORY::class.java.canonicalName, category as Serializable)
            bundle.putSerializable(TYPE::class.java.canonicalName, type as Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }
}