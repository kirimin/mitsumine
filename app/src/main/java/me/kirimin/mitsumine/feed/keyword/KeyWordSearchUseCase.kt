package me.kirimin.mitsumine.feed.keyword

import android.content.Context
import me.kirimin.mitsumine._common.network.FeedRssAccessor
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedUseCase
import rx.Observable

class KeyWordSearchUseCase(context: Context, val keyword: String) : AbstractFeedUseCase(context) {

    override fun getObservable(): Observable<Feed> = FeedRssAccessor.requestKeyword(keyword)
}