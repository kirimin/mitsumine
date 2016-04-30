package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine._common.domain.model.MyBookmark
import rx.subscriptions.CompositeSubscription

class MyBookmarkSearchPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: MyBookmarkSearchView? = null
    private lateinit var repository: MyBookmarkSearchRepository
    private lateinit var keyword: String
    private var totalBookmarkCount = -1

    fun onCreate(view: MyBookmarkSearchView, repository: MyBookmarkSearchRepository, keyword: String) {
        this.view = view
        this.repository = repository
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
        subscriptions.add(repository.requestMyBookmarks(keyword, offset)
                .toList()
                .subscribe({
                    totalBookmarkCount = it[0].totalCount
                    view?.addListViewItem(it)
                }, {
                    view?.showErrorToast()
                    view?.dismissRefreshing()
                }, {
                    view?.dismissRefreshing()
                })
        )
    }
}