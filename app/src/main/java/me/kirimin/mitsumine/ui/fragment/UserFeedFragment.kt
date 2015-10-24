package me.kirimin.mitsumine.ui.fragment


import android.os.Bundle
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.FeedDAO
import me.kirimin.mitsumine.db.NGWordDAO
import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.util.FeedUtil
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

public class UserFeedFragment : AbstractFeedFragment() {

    companion object {

        public fun newFragment(user: String): UserFeedFragment {
            val fragment = UserFeedFragment()
            val bundle = Bundle()
            bundle.putString("user", user)
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
        subscriptions.add(FeedApi.requestUserBookmark(context.applicationContext, arguments.getString("user"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { feed -> !FeedUtil.contains(feed, readFeedList) && !FeedUtil.containsWord(feed, ngWordList) }
                .toList()
                .subscribe({ feedList ->
                    clearFeed()
                    if (feedList.isEmpty() && activity != null) {
                        Toast.makeText(activity, R.string.user_search_toast_notfound, Toast.LENGTH_SHORT).show()
                        dismissRefreshing()
                    } else {
                        setFeed(feedList)
                        dismissRefreshing()
                    }
                }, { e -> }))
        showRefreshing()
    }

    override fun isUseReadLater(): Boolean {
        return true
    }

    override fun isUseRead(): Boolean {
        return true
    }
}