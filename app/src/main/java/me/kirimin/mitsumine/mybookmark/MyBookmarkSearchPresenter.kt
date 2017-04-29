package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine._common.domain.model.MyBookmark
import rx.subscriptions.CompositeSubscription

class MyBookmarkSearchPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: MyBookmarkSearchView? = null
    private lateinit var useCase: MyBookmarkSearchUseCase
    private lateinit var keyword: String
    private var totalBookmarkCount = -1

    fun onCreate(view: MyBookmarkSearchView, useCase: MyBookmarkSearchUseCase, keyword: String) {
        this.view = view
        this.useCase = useCase
        this.keyword = keyword

        view.initViews()
        view.showRefreshing()
        request()
    }

    fun onDestroy() {
        view = null
    }

    fun onRefresh() {
        val view = view ?: return
        view.clearListViewItem()
        view.showRefreshing()
        request()
    }

    fun onScroll(firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int, isRefreshing: Boolean) {
        if (totalItemCount == 0 || totalItemCount >= totalBookmarkCount) return;

        if (firstVisibleItem + visibleItemCount == totalItemCount && !isRefreshing) {
            view?.showRefreshing()
            request(offset = totalItemCount - 1)
        }
    }

    fun onListItemClick(myBookmark: MyBookmark) {
        view?.sendBrowserIntent(myBookmark.linkUrl)
    }

    fun onListItemLongClick(myBookmark: MyBookmark) {
        view?.startEntryInfo(myBookmark.linkUrl)
    }

    private fun request(offset: Int = 0) {
        subscriptions.add(useCase.requestMyBookmarks(keyword, offset)
                .subscribe({
                    val bookmarks = it.first
                    val totalCount = it.second
                    totalBookmarkCount = totalCount
                    view?.addListViewItem(bookmarks)
                    view?.dismissRefreshing()
                }, {
                    it.printStackTrace()
                    view?.showErrorToast()
                    view?.dismissRefreshing()
                })
        )
    }
}