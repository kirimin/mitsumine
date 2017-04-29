package me.kirimin.mitsumine.feed.user

import android.content.Context
import me.kirimin.mitsumine._common.network.FeedRssAccessor
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedUseCase
import rx.Observable

class UserSearchUseCase(context: Context, val user: String) : AbstractFeedUseCase(context) {

    override fun getObservable(): Observable<Feed> = FeedRssAccessor.requestUserBookmark(user)
}