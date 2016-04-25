package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine._common.domain.model.MyBookmark

interface MyBookmarkSearchView {
    fun initViews()
    fun showRefreshing()
    fun addListViewItem(myBookmarks: List<MyBookmark>)
    fun dismissRefreshing()
    fun showErrorToast()
    fun clearListViewItem()
    fun sendBrowserIntent(linkUrl: String)
    fun startEntryInfo(linkUrl: String)
}