package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.network.FeedRssAccessor
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
}