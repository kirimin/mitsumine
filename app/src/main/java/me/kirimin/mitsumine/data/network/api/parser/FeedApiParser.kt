package me.kirimin.mitsumine.data.network.api.parser

import android.util.Log
import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.domain.common.util.toList
import org.json.JSONException
import org.json.JSONObject
import rx.Observable

class FeedApiParser {
    companion object {

        public fun parseResponse(response: JSONObject): Observable<Feed> {
            return try {
                val entries = response.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries")
                val feedList = entries.toList<JSONObject>().map { json -> parseEntryObject(json) }
                Observable.from(feedList)
            } catch (e: JSONException) {
                Observable.empty()
            }
        }

        private fun parseEntryObject(entriesObject: JSONObject): Feed {
            val feed = Feed()
            feed.title = entriesObject.getString("title")
            feed.linkUrl = entriesObject.getString("link").replace("#", "%23")
            feed.content = entriesObject.getString("contentSnippet")
            val content = entriesObject.getString("content")
            feed.thumbnailUrl = parseThumbnailUrl(content)
            feed.bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + feed.linkUrl
            feed.faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + feed.linkUrl
            feed.entryLinkUrl = "http://b.hatena.ne.jp/entry/" + feed.linkUrl
            return feed
        }

        private fun parseThumbnailUrl(content: String): String {
            val urlStartIndex = content.indexOf("http://cdn-ak.b.st-hatena.com/entryimage/")
            if (urlStartIndex != -1) {
                return content.substring(urlStartIndex, content.indexOf("\"", urlStartIndex))
            } else {
                return ""
            }
        }
    }
}