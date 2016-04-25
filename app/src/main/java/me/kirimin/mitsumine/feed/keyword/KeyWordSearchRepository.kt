package me.kirimin.mitsumine.feed.keyword

import android.content.Context
import me.kirimin.mitsumine._common.network.FeedApi
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import rx.Observable

class KeyWordSearchRepository(context: Context, val keyword: String) : AbstractFeedRepository(context) {

    override fun getObservable(): Observable<Feed> = FeedApi.requestKeyword(context, keyword)
}