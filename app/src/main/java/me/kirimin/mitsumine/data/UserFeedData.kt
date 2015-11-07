package me.kirimin.mitsumine.data

import android.content.Context
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.model.Feed
import rx.Observable

open class UserFeedData(context: Context, val user: String) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestUserBookmark(context, user)
}