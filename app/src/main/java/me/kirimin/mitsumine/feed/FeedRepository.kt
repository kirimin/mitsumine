package me.kirimin.mitsumine.feed

import android.content.Context
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.common.database.FeedDAO
import me.kirimin.mitsumine.common.database.NGWordDAO
import me.kirimin.mitsumine.common.network.FeedApi
import me.kirimin.mitsumine.common.domain.util.FeedUtil
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type
import rx.Observable

class FeedRepository(context: Context, val category: Category, val type: Type) : AbstractFeedRepository(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestCategory(context, category, type)
            .filter { feed -> !FeedUtil.contains(feed, readFeedList) && !FeedUtil.containsWord(feed, ngWordList) }

    val readFeedList: List<Feed>
        get() = FeedDAO.findAll()

    val ngWordList: List<String>
        get() = NGWordDAO.findAll()
}