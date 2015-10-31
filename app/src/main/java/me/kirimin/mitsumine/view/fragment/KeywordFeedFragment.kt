package me.kirimin.mitsumine.view.fragment


import android.os.Bundle
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.database.NGWordDAO
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.domain.util.FeedUtil
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

public class KeywordFeedFragment : AbstractFeedFragment() {

    companion object {

        public fun newFragment(keyword: String): KeywordFeedFragment {
            val fragment = KeywordFeedFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val subscriptions = CompositeSubscription()

    override fun onDestroyView() {
        subscriptions.unsubscribe()
        super.onDestroyView()
    }

    override fun requestFeed() {
        val readFeedList = FeedDAO.findAll()
        val ngWordList = NGWordDAO.findAll()
        subscriptions.add(FeedApi.requestKeyword(context.applicationContext, arguments.getString("keyword"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { feed -> !FeedUtil.contains(feed, readFeedList) && !FeedUtil.containsWord(feed, ngWordList) }
                .toList()
                .subscribe ({ feedList ->
                    clearFeed()
                    if (feedList.isEmpty() && activity != null) {
                        Toast.makeText(activity, R.string.keyword_search_toast_notfound, Toast.LENGTH_SHORT).show()
                        dismissRefreshing()
                    } else {
                        setFeed(feedList)
                        dismissRefreshing()
                    }
                }, { e -> dismissRefreshing() }))
        showRefreshing()
    }

    override fun isUseReadLater(): Boolean {
        return true
    }

    override fun isUseRead(): Boolean {
        return true
    }
}