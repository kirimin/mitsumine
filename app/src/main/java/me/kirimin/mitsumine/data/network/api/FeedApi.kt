package me.kirimin.mitsumine.data.network.api

import android.content.Context
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

import rx.Observable

import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.data.network.api.parser.FeedApiParser
import me.kirimin.mitsumine.domain.enums.Category
import me.kirimin.mitsumine.domain.enums.Type

public class FeedApi {

    companion object {

        private val FEED_URL_HEADER = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://b.hatena.ne.jp/"
        private val FEED_URL_FOOTER = ".rss&num=-1"

        public fun requestCategory(context: Context, category: Category, type: Type): Observable<Feed> {
            val observable = ApiAccessor.request(context, FEED_URL_HEADER + type + category + FEED_URL_FOOTER)
            return observable.flatMap { response -> FeedApiParser.parseResponse(response) }
        }

        public fun requestUserBookmark(context: Context, userName: String): Observable<Feed> {
            val observable = ApiAccessor.request(context, FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER)
            return observable.flatMap { response -> FeedApiParser.parseResponse(response) }
        }

        public fun requestKeyword(context: Context, keyword: String): Observable<Feed> {
            val keywordVal = try {
                URLEncoder.encode(keyword, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                keyword
            }

            val observable = ApiAccessor.request(context, FEED_URL_HEADER + "keyword/" + keywordVal + "?mode=rss&num=-1")
            return observable.flatMap { response -> FeedApiParser.parseResponse(response) }
        }
    }
}
