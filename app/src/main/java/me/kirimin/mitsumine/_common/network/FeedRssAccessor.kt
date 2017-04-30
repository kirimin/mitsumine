package me.kirimin.mitsumine._common.network

import rx.Observable

import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine._common.network.entity.FeedRssRoot
import okhttp3.CacheControl
import okhttp3.Request
import org.simpleframework.xml.core.Persister
import java.util.concurrent.TimeUnit

object FeedRssAccessor {

    private val FEED_URL_HEADER = "http://b.hatena.ne.jp/"
    private val FEED_URL_FOOTER = ".rss"
    private val MAX_AGE_SECOND = 60 * 5;

    fun requestCategory(category: Category, type: Type): Observable<Feed> {
        val observable = request(FEED_URL_HEADER + type + category + FEED_URL_FOOTER)
        return observable.flatMap(this::parseResponse)
    }

    fun requestUserFeed(userName: String): Observable<Feed> {
        val observable = request(FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER)
        return observable.flatMap(this::parseResponse)
    }

    fun requestKeywordFeed(keyword: String): Observable<Feed> {
        val observable = request(FEED_URL_HEADER + "keyword/" + keyword + "?mode=rss&num=-1")
        return observable.flatMap(this::parseResponse)
    }

    private fun parseResponse(response: String): Observable<Feed> {
        try {
            val xmlRoot = Persister().read(FeedRssRoot::class.java, response, false)
            return Observable.from(xmlRoot.itemList.map(::Feed))
        } catch(e: Exception) {
            return Observable.error(e)
        }
    }

    private fun request(url: String): Observable<String> {
        return Observable.create<String> { subscriber ->
            val request = Request.Builder()
                    .url(url)
                    .cacheControl(CacheControl.Builder().maxAge(MAX_AGE_SECOND, TimeUnit.SECONDS).build())
                    .get()
                    .build()
            val response = Client.defaultOkHttpClient().newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body().string()
                subscriber.onNext(json)
                subscriber.onCompleted()
            } else {
                subscriber.onError(ApiRequestException("error:" + response.code()));
            }
        }
    }

}
