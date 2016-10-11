package me.kirimin.mitsumine.bookmarklist

import me.kirimin.mitsumine._common.domain.model.Bookmark

interface BookmarkListView {

    fun initViews(bookmarks: List<Bookmark>)

    fun startUserSearchActivity(userId: String)

    fun shareCommentLink(text: String)

    fun showBrowser(url: String)
}