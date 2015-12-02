package me.kirimin.mitsumine.network.api.parser

import android.util.Log
import me.kirimin.mitsumine.model.Feed
import org.json.JSONObject
import rx.Observable
import java.util.*

class FeedApiParser {
    companion object {

        public fun parseResponse(response: String): Observable<Feed> {
            val childList = XmlUtil.parse(response).getDocumentElement().getChildNodes()
            val list = ArrayList<Feed>()
            Log.d("test", childList.getLength().toString())
            for (i in 0..childList.getLength() - 1) {
                val node = childList.item(i)
                if (!"item".equals(node.getNodeName())) {
                    continue
                }
                val feed = Feed()
                val att = node.getChildNodes()
                feed.title = att.item(1).getChildNodes().item(0).getTextContent()
                feed.linkUrl = att.item(3).getChildNodes().item(0).getTextContent().replace("#", "%23")
                feed.content = att.item(5).getChildNodes().item(0).getTextContent()
                val content = att.item(7).getChildNodes().item(0).getTextContent()
                feed.thumbnailUrl = parseThumbnailUrl(content)
                feed.bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + feed.linkUrl
                feed.faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + feed.linkUrl
                feed.entryLinkUrl = "http://b.hatena.ne.jp/entry/" + feed.linkUrl
                list.add(feed)
            }
            return Observable.from(list)
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