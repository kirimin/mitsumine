package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import me.kirimin.mitsumine._common.network.entity.Item
import java.net.URLEncoder

@Table(name = "feed")
class Feed() : Model() {

    @Column(name = "title")
    var title: String = ""

    @Column(name = "thumbnailUrl")
    var thumbnailUrl: String = ""

    @Column(name = "content")
    var content: String = ""

    @Column(name = "linkUrl", unique = true)
    var linkUrl: String = ""

    @Column(name = "entryLinkUrl")
    var entryLinkUrl: String = ""

    @Column(name = "bookmarkCountUrl")
    var bookmarkCountUrl: String = ""

    @Column(name = "faviconUrl")
    var faviconUrl: String = ""

    @Column(name = "type")
    var type: String = ""

    @Column(name = "saveTime")
    var saveTime: Long = 0

    constructor(apiData: Item) : this() {
        title = apiData.title
        linkUrl = apiData.link.replace("#", "%23")
        content = apiData.description ?: ""
        thumbnailUrl = parseThumbnailUrl(apiData.contentEncoded ?: "")
        bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + URLEncoder.encode(linkUrl, "utf-8")
        faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + linkUrl
        entryLinkUrl = "http://b.hatena.ne.jp/entry/" + linkUrl
    }

    override fun toString(): String {
        return StringBuilder().append("title:").append(title).append(" type:").append(type).toString()
    }

    private fun parseThumbnailUrl(content: String): String {
        val urlStartIndex = content.indexOf("http://cdn-ak.b.st-hatena.com/entryimage/")
        if (urlStartIndex != -1) {
            return content.substring(urlStartIndex, content.indexOf("\"", urlStartIndex))
        } else {
            return ""
        }
    }

    companion object {

        const val TYPE_READ: String = "read"
        const val TYPE_READ_LATER: String = "readlater"
    }
}