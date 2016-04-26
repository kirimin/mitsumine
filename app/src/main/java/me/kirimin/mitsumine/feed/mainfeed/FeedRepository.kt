package me.kirimin.mitsumine.feed.mainfeed

import android.content.Context
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine._common.network.FeedApi
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.util.FeedUtil
import rx.Observable

class FeedRepository(context: Context, val category: Category, val type: Type) : AbstractFeedRepository(context) {

    override fun getObservable(): Observable<Feed> = FeedApi.requestCategory(context, category, type)
            .filter { !FeedUtil.contains(it, FeedDAO.findAll()) } // メインフィードの時のみ既読を弾くためここでfilter
}