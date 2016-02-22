package me.kirimin.mitsumine.search

import android.content.Context
import me.kirimin.mitsumine.common.network.FeedApi
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import rx.Observable

class KeyWordFeedRepository(context: Context, val keyword: String) : AbstractFeedRepository(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestKeyword(context, keyword)
}