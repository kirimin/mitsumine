package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.domain.model.Feed
import rx.Subscriber

class FeedPresenter : Subscriber<List<Feed>>() {

    private var view: FeedView? = null
    private lateinit var useCase: FeedUseCase

    fun onCreate(feedView: FeedView, feedUseCase: FeedUseCase) {
        this.view = feedView
        this.useCase = feedUseCase

        feedView.initViews()
        feedView.showRefreshing()
        feedUseCase.requestFeed(this)
    }

    fun onDestroy() {
        view = null
        useCase.unSubscribe()
    }

    fun onRefresh() {
        val view = view ?: return
        view.clearAllItem()
        view.showRefreshing()
        useCase.requestFeed(this)
    }

    override fun onNext(feedList: List<Feed>) {
        view?.setFeed(feedList)
    }

    override fun onError(e: Throwable?) {
        view?.dismissRefreshing()
    }

    override fun onCompleted() {
        view?.dismissRefreshing()
    }

    fun onClick(viewId: Int, feed: Feed) {
        val view = view ?: return
        when (viewId) {
            R.id.card_view -> {
                view.sendUrlIntent(feed.linkUrl)
            }
            R.id.FeedFragmentImageViewShare -> {
                if (useCase.isShareWithTitleSettingEnable()) {
                    view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
                } else {
                    view.sendShareUrlIntent(feed.title, feed.linkUrl)
                }
            }
        }
    }

    fun onLongClick(viewId: Int, feed: Feed): Boolean {
        val view = view ?: return false
        when (viewId) {
            R.id.card_view -> {
                if (useCase.isUseBrowserSettingEnable()) {
                    view.sendUrlIntent(feed.entryLinkUrl)
                } else {
                    view.startEntryInfoView(feed.linkUrl)
                }
                return true
            }
            R.id.FeedFragmentImageViewShare -> {
                if (useCase.isShareWithTitleSettingEnable()) {
                    view.sendShareUrlIntent(feed.title, feed.linkUrl)
                } else {
                    view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
                }
                return true
            }
        }
        return false
    }

    fun onItemClick(feed: Feed) {
        view?.sendUrlIntent(feed.linkUrl)
    }

    fun onItemLongClick(feed: Feed) {
        val view = view ?: return
        if (useCase.isUseBrowserSettingEnable()) {
            view.sendUrlIntent(feed.entryLinkUrl)
        } else {
            view.startEntryInfoView(feed.linkUrl)
        }
    }

    fun onFeedShareClick(feed: Feed) {
        val view = view ?: return
        if (useCase.isShareWithTitleSettingEnable()) {
            view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        } else {
            view.sendShareUrlIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedShareLongClick(feed: Feed) {
        val view = view ?: return
        if (useCase.isShareWithTitleSettingEnable()) {
            view.sendShareUrlIntent(feed.title, feed.linkUrl)
        } else {
            view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedLeftSlide(holder: FeedAdapter.ViewHolder, feed: Feed, useReadLater: Boolean) {
        val view = view ?: return
        useCase.saveFeed(feed, Feed.TYPE_READ)
        if (useReadLater) {
            view.setListViewCellPagerPosition(holder, 1)
        } else {
            view.setListViewCellPagerPosition(holder, 0)
        }
        view.removeItem(feed)
    }

    fun onFeedRightSlide(holder: FeedAdapter.ViewHolder, feed: Feed) {
        val view = view ?: return
        useCase.saveFeed(feed, Feed.TYPE_READ_LATER)
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

        holder.tags.tag = useCase.requestTagList(object : Subscriber<String>() {
            override fun onNext(tags: String) {
                view.setTagList(holder, tags)
            }

            override fun onError(e: Throwable) {
            }

            override fun onCompleted() {
            }
        }, item.linkUrl)
        holder.tags.tag = useCase.requestBookmarkCount(object : Subscriber<String>() {
            override fun onNext(count: String) {
                view.setBookmarkCount(holder, count)
            }

            override fun onError(e: Throwable) {
            }

            override fun onCompleted() {
            }
        }, item.linkUrl)
    }
}
