package me.kirimin.mitsumine.domain.usecase

import me.kirimin.mitsumine.data.FeedData
import me.kirimin.mitsumine.domain.common.util.FeedUtil
import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.model.enums.Category
import me.kirimin.mitsumine.model.enums.Type
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class FeedUseCase(val data: FeedData) {

    private var subscriptions = CompositeSubscription()

    fun requestFeed(category: Category, type: Type, subscriber: Subscriber<List<Feed>>) {
        subscriptions.add(data.requestFeed(category, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { feed -> !FeedUtil.contains(feed, data.readFeedList) && !FeedUtil.containsWord(feed, data.ngWordList) }
                .toList()
                .subscribe(subscriber))
    }

    fun unSubscribe() {
        subscriptions.unsubscribe()
    }

    val isUseBrowserSettingEnable: Boolean
        get() = data.isUseBrowserSettingEnable

    val isShareWithTitleSettingEnable: Boolean
        get() = data.isShareWithTitleSettingEnable

    fun saveFeed(feed: Feed, type: String) {
        feed.type = type
        data.saveFeed(feed)
    }
}