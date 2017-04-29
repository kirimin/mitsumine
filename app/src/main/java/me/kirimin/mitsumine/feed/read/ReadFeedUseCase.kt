package me.kirimin.mitsumine.feed.read

import android.content.Context
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine.feed.AbstractFeedUseCase
import rx.Observable

open class ReadFeedUseCase(context: Context, val type: String) : AbstractFeedUseCase(context) {

    override fun getObservable() = Observable.from(FeedDAO.findByType(type))
}