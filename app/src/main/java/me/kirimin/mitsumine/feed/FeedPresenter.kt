package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.util.FeedUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class FeedPresenter {

    private val subscriptions = CompositeSubscription()

    private var view: FeedView? = null
    private lateinit var repository: AbstractFeedRepository

    fun onCreate(feedView: FeedView, repository: AbstractFeedRepository) {
        this.view = feedView
        this.repository = repository

        feedView.initViews()
        feedView.showRefreshing()
        requestFeed()
    }

    fun onDestroy() {
        view = null
        subscriptions.unsubscribe()
    }

    fun onRefresh() {
        val view = view ?: return
        view.clearAllItem()
        view.showRefreshing()
        requestFeed()
    }

    fun onItemClick(feed: Feed) {
        view?.sendUrlIntent(feed.linkUrl)
    }

    fun onItemLongClick(feed: Feed) {
        val view = view ?: return
        if (repository.isUseBrowserSettingEnable) {
            view.sendUrlIntent(feed.entryLinkUrl)
        } else {
            view.startEntryInfoView(feed.linkUrl)
        }
    }

    fun onFeedShareClick(feed: Feed) {
        val view = view ?: return
        if (repository.isShareWithTitleSettingEnable) {
            view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        } else {
            view.sendShareUrlIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedShareLongClick(feed: Feed) {
        val view = view ?: return
        if (repository.isShareWithTitleSettingEnable) {
            view.sendShareUrlIntent(feed.title, feed.linkUrl)
        } else {
            view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedLeftSlide(holder: FeedAdapter.ViewHolder, feed: Feed, useReadLater: Boolean) {
        val view = view ?: return
        feed.type = Feed.TYPE_READ
        repository.saveFeed(feed)
        if (useReadLater) {
            view.setListViewCellPagerPosition(holder, 1)
        } else {
            view.setListViewCellPagerPosition(holder, 0)
        }
        view.removeItem(feed)
    }

    fun onFeedRightSlide(holder: FeedAdapter.ViewHolder, feed: Feed) {
        val view = view ?: return
        feed.type = Feed.TYPE_READ_LATER
        repository.saveFeed(feed)
        view.setListViewCellPagerPosition(holder, 1)
        view.removeItem(feed)
    }

    fun onGetView(holder: FeedAdapter.ViewHolder, item: Feed) {
        val view = view ?: return
        view.initListViewCell(holder, item)
        if (!item.thumbnailUrl.isEmpty()) {
            view.loadThumbnailImage(holder, item.thumbnailUrl)
        }
        if (!item.faviconUrl.isEmpty()) {
            view.loadFaviconImage(holder, item.faviconUrl)
        }

        holder.tags.tag = repository.requestTagList(item.linkUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.tagListString }
                .subscribe(object : Subscriber<String>() {
                    override fun onNext(tags: String) {
                        view.setTagList(holder, tags)
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onCompleted() {
                    }
                })
        holder.bookmarkCount.tag = repository.requestBookmarkCount(item.linkUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onNext(count: String) {
                        view.setBookmarkCount(holder, count)
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onCompleted() {
                    }
                })
    }

    private fun requestFeed() {
        subscriptions.add(repository.requestFeed()
                .toList()
                .subscribe({
                    view?.setFeed(it.filter { !FeedUtil.containsWord(it, repository.ngWordList) })
                    view?.dismissRefreshing()
                }, { e ->
                    view?.dismissRefreshing()
                })
        )
    }
}
