package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine.mybookmark.MyBookmarkSearchUseCase
import me.kirimin.mitsumine._common.domain.model.MyBookmark
import me.kirimin.mitsumine.mybookmark.MyBookmarkSearchView
import rx.Observer

class MyBookmarkSearchPresenter : Observer<List<MyBookmark>> {

    private var view: MyBookmarkSearchView? = null
    private var useCase: MyBookmarkSearchUseCase? = null

    private var keyword: String? = null

    fun onCreate(myBookmarksView: MyBookmarkSearchView, myBookmarksUseCase: MyBookmarkSearchUseCase, keyword: String) {
        view = myBookmarksView
        useCase = myBookmarksUseCase
        this.keyword = keyword

        view!!.initViews()
        view!!.showRefreshing()
        useCase!!.requestMyBookmarks(this, keyword, 0)
    }

    fun onDestroy() {
        view = null
    }

    override fun onNext(myBookmarks: List<MyBookmark>) {
        view?.addListViewItem(myBookmarks)
    }

    override fun onError(e: Throwable?) {
        view?.showErrorToast()
        view?.dismissRefreshing()
    }

    override fun onCompleted() {
        view?.dismissRefreshing()
    }

    fun onRefresh() {
        view?.clearListViewItem()
        view?.showRefreshing()
        useCase!!.requestMyBookmarks(this, keyword!!, 0)
    }

    fun onScroll(firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int, isRefreshing: Boolean) {
        if (totalItemCount == 0) return;

        if (firstVisibleItem + visibleItemCount == totalItemCount && !isRefreshing) {
            view?.showRefreshing()
            useCase!!.requestMyBookmarks(this, keyword!!, totalItemCount - 1)
        }
    }

    fun onListItemClick(myBookmark: MyBookmark) {
        view?.sendBrowserIntent(myBookmark.linkUrl)
    }

    fun onListItemLongClick(myBookmark: MyBookmark) {
        view?.startEntryInfo(myBookmark.linkUrl)
    }
}