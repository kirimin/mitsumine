package me.kirimin.mitsumine.common.network.parser

import me.kirimin.mitsumine.common.domain.model.Feed
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import rx.Observable
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

object FeedApiParser {

    fun parseResponse(response: String): Observable<Feed> {
        val childList = try {
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(ByteArrayInputStream(response.toByteArray())))
            document.documentElement.childNodes
        } catch (e: ParserConfigurationException) {
            return Observable.empty()
        } catch (e: SAXException) {
            return Observable.empty()
        } catch (e: IOException) {
            return Observable.empty()
        }

        val list = ArrayList<Feed>()
        for (i in 0..childList.length - 1) {
            val node = childList.item(i)
            if (!"item".equals(node.getNodeName())) {
                continue
            }
            try {
                val feed = Feed()
                val att = node.childNodes
                feed.title = att.item(1).childNodes.item(0).textContent
                feed.linkUrl = att.item(3).childNodes.item(0).textContent.replace("#", "%23")
                feed.content = att.item(5).childNodes.item(0).textContent
                val content = att.item(7).childNodes.item(0).textContent
                feed.thumbnailUrl = parseThumbnailUrl(content)
                feed.bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + feed.linkUrl
                feed.faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + feed.linkUrl
                feed.entryLinkUrl = "http://b.hatena.ne.jp/entry/" + feed.linkUrl
                list.add(feed)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return Observable.from(list)
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