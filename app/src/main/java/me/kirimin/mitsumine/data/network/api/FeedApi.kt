package me.kirimin.mitsumine.data.network.api

import android.content.Context
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

import rx.Observable

import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.data.network.api.parser.FeedApiParser
import me.kirimin.mitsumine.domain.enums.Category
import me.kirimin.mitsumine.domain.enums.Type

object FeedApi {

    private val FEED_URL_HEADER = "http://b.hatena.ne.jp/"
    private val FEED_URL_FOOTER = ".rss"

    fun requestCategory(context: Context, category: Category, type: Type): Observable<Feed> {
        val observable = ApiAccessor.stringRequest(context, FEED_URL_HEADER + type + category + FEED_URL_FOOTER)
        return observable.flatMap { response -> FeedApiParser.parseResponse(response) }
    }

    fun requestUserBookmark(context: Context, userName: String): Observable<Feed> {
        val observable = ApiAccessor.stringRequest(context, FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER)
        return observable.flatMap { response -> FeedApiParser.parseResponse(response) }
    }

    fun requestKeyword(context: Context, keyword: String): Observable<Feed> {
        val keywordVal = try {
            URLEncoder.encode(keyword, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            keyword
        }

        val observable = ApiAccessor.stringRequest(context, FEED_URL_HEADER + "keyword/" + keywordVal + "?mode=rss&num=-1")
        return observable.flatMap { response -> FeedApiParser.parseResponse(response) }
    }
}
