package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.util.FeedUtil
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class FeedPresenter @Inject constructor(val useCase: FeedUseCase) {

    private val subscriptions = CompositeSubscription()

    lateinit var view: FeedView get
    lateinit var method: FeedMethod

    fun onCreate(method: FeedMethod) {
        this.method = method
        view.initViews(isUseRead = method.isUseRead, isUseReadLater = method.isUseReadLater)
        view.showRefreshing()
        requestFeed()
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
    }

    fun onRefresh() {
        view.clearAllItem()
        view.showRefreshing()
        requestFeed()
    }

    fun onItemClick(feed: Feed) {
        view.sendUrlIntent(feed.linkUrl)
    }

    fun onItemLongClick(feed: Feed) {
        if (useCase.isUseBrowserSettingEnable) {
            view.sendUrlIntent(feed.entryLinkUrl)
        } else {
            view.startEntryInfoView(feed.linkUrl)
        }
    }

    fun onFeedShareClick(feed: Feed) {
        if (useCase.isShareWithTitleSettingEnable) {
            view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        } else {
            view.sendShareUrlIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedShareLongClick(feed: Feed) {
        if (useCase.isShareWithTitleSettingEnable) {
            view.sendShareUrlIntent(feed.title, feed.linkUrl)
        } else {
            view.sendShareUrlWithTitleIntent(feed.title, feed.linkUrl)
        }
    }

    fun onFeedLeftSlide(holder: FeedAdapter.ViewHolder, feed: Feed, useReadLater: Boolean) {
        feed.type = Feed.TYPE_READ
        useCase.saveFeed(feed)
        if (useReadLater) {
            view.setListViewCellPagerPosition(holder, 1)
        } else {
            view.setListViewCellPagerPosition(holder, 0)
        }
        view.removeItem(feed)
    }

    fun onFeedRightSlide(holder: FeedAdapter.ViewHolder, feed: Feed) {
        feed.type = Feed.TYPE_READ_LATER
        useCase.saveFeed(feed)
        view.setListViewCellPagerPosition(holder, 1)
        view.removeItem(feed)
    }

    fun onTutorialTap() {
        view.hideTutorial()
        useCase.isFirstBoot = false
    }

    fun onGetView(holder: FeedAdapter.ViewHolder, item: Feed) {
        view.initListViewCell(holder, item)
        if (item.thumbnailUrl.isNotEmpty()) {
            view.loadThumbnailImage(holder, item.thumbnailUrl)
        }
        if (item.faviconUrl.isNotEmpty()) {
            view.loadFaviconImage(holder, item.faviconUrl)
        }

        holder.tags.tag = useCase.requestTagList(item.linkUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onNext(tags: String) {
                        view.setTagList(holder, tags)
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onCompleted() {
                    }
                })
        holder.bookmarkCount.tag = useCase.requestBookmarkCount(item.linkUrl)
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
        val method = method
        val observable = when (method) {
            is FeedMethod.MainFeed -> useCase.requestMainFeed(method.category, method.type)
            is FeedMethod.KeywordSearch -> useCase.requestKeywordFeed(method.keyword)
            is FeedMethod.UserSearch -> useCase.requestUserFeed(method.user)
            is FeedMethod.Read -> useCase.requestReadFeed()
            is FeedMethod.ReadLatter -> useCase.requestReadLatterFeed()
        }
        subscriptions.add(observable.toList()
                .subscribe({
                    view.setFeed(it.filter { !FeedUtil.containsWord(it, useCase.ngWordList) })
                    view.dismissRefreshing()
                    if (useCase.isFirstBoot) {
                        view.showTutorial()
                    }
                }, { e ->
                    view.dismissRefreshing()
                    view.showError(R.string.network_error)
                }))
    }

    sealed class FeedMethod(val isUseReadLater: Boolean, val isUseRead: Boolean) {
        class MainFeed(val type: Type, val category: Category) : FeedMethod(true, true)
        class KeywordSearch(val keyword: String) : FeedMethod(true, true)
        class UserSearch(val user: String) : FeedMethod(true, true)
        class Read : FeedMethod(true, false)
        class ReadLatter : FeedMethod(false, true)
    }
}
