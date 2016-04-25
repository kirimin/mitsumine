package me.kirimin.mitsumine.feed.keyword

import android.content.Context
import me.kirimin.mitsumine.common.network.FeedApi
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import rx.Observable

class KeyWordSearchRepository(context: Context, val keyword: String) : AbstractFeedRepository(context) {

    override fun getObservable(): Observable<Feed> = FeedApi.requestKeyword(context, keyword)
}