package me.kirimin.mitsumine.presenter

import me.kirimin.mitsumine.domain.model.Bookmark
import me.kirimin.mitsumine.view.BookmarkListView

class BookmarkListPresenter {

    var view: BookmarkListView? = null

    fun onCreate(bookmarkListView: BookmarkListView, bookmarks: List<Bookmark>){
        this.view = bookmarkListView
        view!!.initViews(bookmarks)
    }

    fun onItemClick(bookmark: Bookmark) {
        view?.startUserSearchActivity(bookmark.user)
    }
}