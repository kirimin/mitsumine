package me.kirimin.mitsumine.feed.read

import android.content.Context
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import rx.Observable

open class ReadFeedRepository(context: Context, val type: String) : AbstractFeedRepository(context) {

    override fun getObservable() = Observable.from(FeedDAO.findByType(type))
}