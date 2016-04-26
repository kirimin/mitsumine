package me.kirimin.mitsumine.feed.mainfeed

import android.content.Context
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine._common.network.FeedApi
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import rx.Observable

class FeedRepository(context: Context, val category: Category, val type: Type) : AbstractFeedRepository(context) {

    override fun getObservable(): Observable<Feed> = FeedApi.requestCategory(context, category, type)
}