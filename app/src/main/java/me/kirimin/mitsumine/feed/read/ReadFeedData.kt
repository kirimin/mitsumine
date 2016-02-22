package me.kirimin.mitsumine.feed.read

import android.content.Context
import me.kirimin.mitsumine.common.database.FeedDAO
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedData
import rx.Observable

open class ReadFeedData(context: Context, val type: String) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = Observable.from(FeedDAO.findByType(type))
}