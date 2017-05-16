package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.network.FeedRssAccessor
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class FeedRepository @Inject constructor() {

    fun requestFeed(category: Category, type: Type) = FeedRssAccessor.requestCategory(category, type)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!


    fun requestUserFeed(user: String) = FeedRssAccessor.requestUserFeed(user)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestKeywordFeed(keyword: String) = FeedRssAccessor.requestKeywordFeed(keyword)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestReadFeed() = Observable.from(FeedDAO.findByType(Feed.TYPE_READ))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestReadLatterFeed() = Observable.from(FeedDAO.findByType(Feed.TYPE_READ_LATER))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!
}