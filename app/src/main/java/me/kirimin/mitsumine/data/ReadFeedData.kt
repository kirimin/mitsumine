package me.kirimin.mitsumine.data

import android.content.Context
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.model.Feed
import rx.Observable

open class ReadFeedData(context: Context, val type: String) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = Observable.from(FeedDAO.findByType(type))
}