package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine._common.domain.model.Feed

interface FeedView {
    fun initViews(isUseRead: Boolean, isUseReadLater: Boolean)
    fun showRefreshing()
    fun clearAllItem()
    fun setFeed(feedList: List<Feed>)
    fun dismissRefreshing()
    fun sendUrlIntent(url: String)
    fun startEntryInfoView(url: String)
    fun sendShareUrlIntent(title: String, url: String)
    fun sendShareUrlWithTitleIntent(title: String, url: String)
    fun removeItem(feed: Feed)
    fun initListViewCell(holder: FeedAdapter.ViewHolder, feed: Feed)
    fun setListViewCellPagerPosition(holder: FeedAdapter.ViewHolder, position: Int)
    fun setTagList(holder: FeedAdapter.ViewHolder, tags: String)
    fun setBookmarkCount(holder: FeedAdapter.ViewHolder, count: String)
    fun loadThumbnailImage(holder: FeedAdapter.ViewHolder, url: String)
    fun loadFaviconImage(holder: FeedAdapter.ViewHolder, url: String)
}