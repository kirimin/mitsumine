package me.kirimin.mitsumine.view.fragment


import android.os.Bundle

import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.database.NGWordDAO
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.domain.util.FeedUtil
import me.kirimin.mitsumine.model.Category
import me.kirimin.mitsumine.model.Type
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
        val category = arguments.getSerializable(Category::class.java.canonicalName) as Category
        val type = arguments.getSerializable(Type::class.java.canonicalName) as Type
        val readFeedList = FeedDAO.findAll()
        val ngWordList = NGWordDAO.findAll()
        subscriptions.add(FeedApi.requestCategory(context.applicationContext, category, type)
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

        public fun newFragment(category: Category, type: Type): FeedFragment {
            val fragment = FeedFragment()
            val bundle = Bundle()
            bundle.putSerializable(Category::class.java.canonicalName, category as Serializable)
            bundle.putSerializable(Type::class.java.canonicalName, type as Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }
}