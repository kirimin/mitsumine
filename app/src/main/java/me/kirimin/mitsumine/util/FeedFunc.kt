package me.kirimin.mitsumine.util

import org.json.JSONException
import org.json.JSONObject

import me.kirimin.mitsumine.model.Feed
import rx.Observable
import toList

public class FeedFunc {
    companion object {

        public fun jsonToObservable(response: JSONObject): Observable<Feed> {
            try {
                val entries = response.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries")
                val feedList = entries.toList<JSONObject>().map { obj -> parseFeed(obj) }
                return Observable.from(feedList)
            } catch (e: JSONException) {
                return Observable.empty()
            }
        }

        public fun contains(target: Feed, list: List<Feed>): Boolean {
            return list.filter { f -> f.title == target.title }.count() >= 1
        }

        public fun containsWord(target: Feed, list: List<String>): Boolean {
            return list.filter { s -> target.title.contains(s) || target.linkUrl.contains(s) }.count() >= 1
        }

        throws(JSONException::class)
        private fun parseFeed(entriesObject: JSONObject): Feed {
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
