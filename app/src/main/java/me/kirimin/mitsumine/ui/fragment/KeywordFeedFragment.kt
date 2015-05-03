package me.kirimin.mitsumine.ui.fragment


import android.os.Bundle
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.FeedDAO
import me.kirimin.mitsumine.db.NGWordDAO
import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.network.RequestQueueSingleton
import me.kirimin.mitsumine.util.FeedFunc
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

public class KeywordFeedFragment : AbstractFeedFragment() {

    companion object {

        public fun newFragment(keyword: String): KeywordFeedFragment {
            val fragment = KeywordFeedFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.setArguments(bundle)
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
        subscriptions.add(FeedApi.requestKeyword(RequestQueueSingleton.get(getActivity()), getArguments().getString("keyword"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap<Feed>(FeedFunc.mapToFeedList())
                .filter(FeedFunc.notContains(readFeedList))
                .filter(FeedFunc.notContainsWord(ngWordList))
                .toList()
                .subscribe ({ feedList ->
                    clearFeed()
                    if (feedList.isEmpty() && getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.keyword_search_toast_notfound, Toast.LENGTH_SHORT).show()
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