package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.common.domain.model.Feed
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

open class FeedUseCase(val repository: AbstractFeedRepository) {

    val subscriptions = CompositeSubscription()

    open fun isUseBrowserSettingEnable() = repository.isUseBrowserSettingEnable

    open fun isShareWithTitleSettingEnable() = repository.isShareWithTitleSettingEnable

    open fun requestFeed(subscriber: Observer<List<Feed>>) {
        subscriptions.add(repository.requestFeed()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(subscriber))
    }

    open fun requestTagList(subscriber: Observer<String>, url: String): Subscription =
            repository.requestTagList(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.joinToString(", ") }
                    .subscribe(subscriber)

    open fun requestBookmarkCount(subscriber: Observer<String>, url: String): Subscription =
            repository.requestBookmarkCount(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber)

    fun unSubscribe() {
        subscriptions.unsubscribe()
    }

    open fun saveFeed(feed: Feed, type: String) {
        feed.type = type
        repository.saveFeed(feed)
    }
}