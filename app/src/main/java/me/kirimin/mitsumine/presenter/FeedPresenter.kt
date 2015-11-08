package me.kirimin.mitsumine.presenter

import me.kirimin.mitsumine.domain.FeedUseCase
import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.view.FeedView
import rx.Subscriber

class FeedPresenter : Subscriber<List<Feed>>() {

    var view: FeedView? = null
    var useCase: FeedUseCase? = null

    fun onCreate(feedView: FeedView, feedUseCase: FeedUseCase) {
        this.view = feedView
        this.useCase = feedUseCase


        view?.initViews()
        view?.showRefreshing()
        useCase?.requestFeed(this)
    }

    fun onDestroy() {
        view = null
        useCase?.unSubscribe()
    }

    fun onRefresh() {
        view?.clearAllItem()
        view?.showRefreshing()
        useCase?.requestFeed(this)
    }

    override fun onNext(feedList: List<Feed>) {
        view?.setFeed(feedList)
        view?.dismissRefreshing()
    }

    override fun onError(e: Throwable?) {
        view?.dismissRefreshing()
    }

    override fun onCompleted() {
    }

    fun onItemClick(feed: Feed) {
        view?.sendUrlIntent(feed.linkUrl)
    }

    fun onItemLongClick(feed: Feed) {
        if (useCase!!.isUseBrowserSettingEnable()) {
            view?.sendUrlIntent(feed.entryLinkUrl)
        } else {
            view?.startEntryInfoView(feed.linkUrl)
        }
    }

    fun onFeedShareClick(feed: Feed) {
        if (useCase!!.isShareWithTitleSettingEnable()) {
            view?.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        } else {
            view?.sendShareUrlIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedShareLongClick(feed: Feed) {
        if (useCase!!.isShareWithTitleSettingEnable()) {
            view?.sendShareUrlIntent(feed.title, feed.linkUrl)
        } else {
            view?.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedLeftSlide(feed: Feed) {
        useCase!!.saveFeed(feed, Feed.TYPE_READ)
        view?.removeItem(feed)
    }

    fun onFeedRightSlide(feed: Feed) {
        useCase!!.saveFeed(feed, Feed.TYPE_READ_LATER)
        view?.removeItem(feed)
    }
}