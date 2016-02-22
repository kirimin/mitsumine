package me.kirimin.mitsumine.search

import android.content.Context
import me.kirimin.mitsumine.common.network.FeedApi
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedData
import rx.Observable

class KeyWordFeedData(context: Context, val keyword: String) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestKeyword(context, keyword)
}