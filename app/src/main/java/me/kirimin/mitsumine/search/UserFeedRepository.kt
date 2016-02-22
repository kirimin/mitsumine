package me.kirimin.mitsumine.search

import android.content.Context
import me.kirimin.mitsumine.common.network.FeedApi
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import rx.Observable

open class UserFeedRepository(context: Context, val user: String) : AbstractFeedRepository(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestUserBookmark(context, user)
}