package me.kirimin.mitsumine.bookmarklist

import me.kirimin.mitsumine._common.domain.model.Bookmark

class BookmarkListPresenter {

    var view: BookmarkListView? = null

    fun onCreate(bookmarkListView: BookmarkListView, bookmarks: List<Bookmark>) {
        this.view = bookmarkListView
        bookmarkListView.initViews(bookmarks)
    }

    fun onMoreIconClick(bookmark: Bookmark, id: String, index: Int) {
        when (index) {
            BookmarkPopupWindowBuilder.INDEX_SHARE -> {
                val url = "http://b.hatena.ne.jp/entry/$id/comment/${bookmark.user}"
                view?.shareCommentLink("\"${bookmark.comment.toString()}\" id:${bookmark.user} $url")
            }
            BookmarkPopupWindowBuilder.INDEX_BROWSER -> {
                val url = "http://b.hatena.ne.jp/entry/$id/comment/${bookmark.user}"
                view?.showBrowser(url)
            }
            BookmarkPopupWindowBuilder.INDEX_SEARCH -> {
                view?.startUserSearchActivity(bookmark.user)
            }
        }
    }
}