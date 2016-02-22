package me.kirimin.mitsumine.search

import android.content.Context
import me.kirimin.mitsumine.common.network.FeedApi
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedData
import rx.Observable

open class UserFeedData(context: Context, val user: String) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestUserBookmark(context, user)
}