package me.kirimin.mitsumine.view

import me.kirimin.mitsumine.model.Bookmark

interface BookmarkListView {

    open fun initViews(bookmarks: List<Bookmark>)

    open fun startUserSearchActivity(userId: String)
}