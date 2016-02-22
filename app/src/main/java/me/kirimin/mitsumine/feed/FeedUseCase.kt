package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine.feed.AbstractFeedData
import me.kirimin.mitsumine.common.domain.model.Feed
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

open class FeedUseCase(val data: AbstractFeedData) {

    val subscriptions = CompositeSubscription()

    open fun isUseBrowserSettingEnable() = data.isUseBrowserSettingEnable

    open fun isShareWithTitleSettingEnable() = data.isShareWithTitleSettingEnable

    open fun requestFeed(subscriber: Observer<List<Feed>>) {
        subscriptions.add(data.requestFeed()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(subscriber))
    }

    open fun requestTagList(subscriber: Observer<String>, url: String): Subscription =
            data.requestTagList(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.joinToString(", ") }
                    .subscribe(subscriber)

    open fun requestBookmarkCount(subscriber: Observer<String>, url: String): Subscription =
            data.requestBookmarkCount(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber)

    fun unSubscribe() {
        subscriptions.unsubscribe()
    }

    open fun saveFeed(feed: Feed, type: String) {
        feed.type = type
        data.saveFeed(feed)
    }
}