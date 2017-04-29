package me.kirimin.mitsumine.feed.mainfeed

import android.content.Context
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine.feed.AbstractFeedUseCase
import me.kirimin.mitsumine._common.network.FeedRssAccessor
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.util.FeedUtil
import rx.Observable

class FeedUseCase(context: Context, val category: Category, val type: Type) : AbstractFeedUseCase(context) {

    override fun getObservable(): Observable<Feed> = FeedRssAccessor.requestCategory(category, type)
            .filter { !FeedUtil.contains(it, FeedDAO.findAll()) } // メインフィードの時のみ既読を弾くためここでfilter
}