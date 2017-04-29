package me.kirimin.mitsumine.feed.user

import android.content.Context
import me.kirimin.mitsumine._common.network.FeedRssAccessor
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import rx.Observable

class UserSearchRepository(context: Context, val user: String) : AbstractFeedRepository(context) {

    override fun getObservable(): Observable<Feed> = FeedRssAccessor.requestUserBookmark(user)
}