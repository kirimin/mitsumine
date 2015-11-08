package me.kirimin.mitsumine.domain

import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.FeedData
import me.kirimin.mitsumine.domain.common.util.FeedUtil
import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.domain.enums.Category
import me.kirimin.mitsumine.domain.enums.Type
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class FeedUseCase(val data: AbstractFeedData) {

    val subscriptions = CompositeSubscription()

    fun isUseBrowserSettingEnable() = data.isUseBrowserSettingEnable

    fun isShareWithTitleSettingEnable() = data.isShareWithTitleSettingEnable

    fun requestFeed(subscriber: Observer<List<Feed>>) {
        subscriptions.add(data.requestFeed()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(subscriber))
    }

    fun unSubscribe() {
        subscriptions.unsubscribe()
    }

    fun saveFeed(feed: Feed, type: String) {
        feed.type = type
        data.saveFeed(feed)
    }
}