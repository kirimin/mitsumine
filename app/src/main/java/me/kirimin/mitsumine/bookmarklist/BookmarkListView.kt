package me.kirimin.mitsumine.bookmarklist

import me.kirimin.mitsumine._common.domain.model.Bookmark

interface BookmarkListView {

    open fun initViews(bookmarks: List<Bookmark>)

    open fun startUserSearchActivity(userId: String)
}