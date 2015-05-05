package me.kirimin.mitsumine.network.api

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

import me.kirimin.mitsumine.R
import rx.Observable

import org.json.JSONObject

import com.android.volley.RequestQueue

public class FeedApi {

    public enum class TYPE private(private val TEXT: String) {
        HOT : TYPE("hotentry")
        NEW : TYPE("entrylist")

        override fun toString(): String {
            return TEXT
        }
    }

    public enum class CATEGORY private(private val url: String, public val labelResource: Int) {
        MAIN : CATEGORY("", R.string.feed_main)
        SOCIAL : CATEGORY("/social", R.string.feed_social)
        ECONOMICS : CATEGORY("/economics", R.string.feed_economics)
        LIFE : CATEGORY("/life", R.string.feed_life)
        KNOWLEDGE : CATEGORY("/knowledge", R.string.feed_knowledge)
        IT : CATEGORY("/it", R.string.feed_it)
        FUN : CATEGORY("/fun", R.string.feed_fun)
        ENTERTAINMENT : CATEGORY("/entertainment", R.string.feed_entertainment)
        GAME : CATEGORY("/game", R.string.feed_game)

        override fun toString(): String {
            return url
        }
    }

    companion object {

        private val FEED_URL_HEADER = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://b.hatena.ne.jp/"
        private val FEED_URL_FOOTER = ".rss&num=-1"

        public fun requestCategory(requestQueue: RequestQueue, category: CATEGORY, type: TYPE): Observable<JSONObject> {
            return ApiAccessor.request(requestQueue, FEED_URL_HEADER + type + category + FEED_URL_FOOTER)
        }

        public fun requestUserBookmark(requestQueue: RequestQueue, userName: String): Observable<JSONObject> {
            return ApiAccessor.request(requestQueue, FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER)
        }

        public fun requestKeyword(requestQueue: RequestQueue, keyword: String): Observable<JSONObject> {
            val keywordVal = try {
                URLEncoder.encode(keyword, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                keyword
            }

            return ApiAccessor.request(requestQueue, FEED_URL_HEADER + "keyword/" + keywordVal + "?mode=rss&num=-1")
        }
    }
}
