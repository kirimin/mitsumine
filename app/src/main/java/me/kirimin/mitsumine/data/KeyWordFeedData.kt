package me.kirimin.mitsumine.data

import android.content.Context
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.domain.model.Feed
import rx.Observable

class KeyWordFeedData(context: Context, val keyword: String) : AbstractFeedData(context) {

    override fun requestFeed(): Observable<Feed> = FeedApi.requestKeyword(context, keyword)
}