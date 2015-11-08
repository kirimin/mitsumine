package me.kirimin.mitsumine.view

import me.kirimin.mitsumine.domain.model.MyBookmark

interface MyBookmarksView {
    fun initViews()
    fun showRefreshing()
    fun addListViewItem(myBookmarks: List<MyBookmark>)
    fun dismissRefreshing()
    fun showErrorToast()
    fun clearListViewItem()
    fun sendBrowserIntent(linkUrl: String)
    fun startEntryInfo(linkUrl: String)
}