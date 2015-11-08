package me.kirimin.mitsumine.data

import android.content.Context
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.database.NGWordDAO
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.domain.common.util.FeedUtil
import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.domain.enums.Category
import me.kirimin.mitsumine.domain.enums.Type
import rx.Observable

class FeedData(context: Context, val category: Category, val type: Type) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestCategory(context, category, type)
            .filter { feed -> !FeedUtil.contains(feed, readFeedList) && !FeedUtil.containsWord(feed, ngWordList) }

    val readFeedList: List<Feed>
        get() = FeedDAO.findAll()

    val ngWordList: List<String>
        get() = NGWordDAO.findAll()
}