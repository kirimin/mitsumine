package me.kirimin.mitsumine.view

import me.kirimin.mitsumine.domain.model.Feed

interface FeedView {
    fun initViews()
    fun showRefreshing()
    open fun clearAllItem()
    open fun setFeed(feedList: List<Feed>)
    open fun dismissRefreshing()
    open fun sendUrlIntent(url: String)
    open fun startEntryInfoView(url: String)
    open fun sendShareUrlIntent(title: String, url: String)
    open fun sendShareUrlWithTitleIntent(title: String, url: String)
    open fun removeItem(feed: Feed)
}