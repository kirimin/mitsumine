package me.kirimin.mitsumine.view

import me.kirimin.mitsumine.domain.model.Bookmark

interface BookmarkListView {

    open fun initViews(bookmarks: List<Bookmark>)

    open fun startUserSearchActivity(userId: String)
}