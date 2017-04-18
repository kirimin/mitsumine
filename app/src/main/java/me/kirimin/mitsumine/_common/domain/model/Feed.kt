package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import me.kirimin.mitsumine._common.network.entity.FeedEntity
import java.net.URLEncoder

@Table(name = "feed")
open class Feed() : Model() {

    @Column(name = "title")
    open var title: String = ""

    @Column(name = "thumbnailUrl")
    open var thumbnailUrl: String = ""

    @Column(name = "content")
    open var content: String = ""

    @Column(name = "linkUrl", unique = true)
    open var linkUrl: String = ""

    @Column(name = "entryLinkUrl")
    open var entryLinkUrl: String = ""

    @Column(name = "bookmarkCountUrl")
    open var bookmarkCountUrl: String = ""

    @Column(name = "faviconUrl")
    open var faviconUrl: String = ""

    @Column(name = "type")
    open var type: String = ""

    @Column(name = "saveTime")
    open var saveTime: Long = 0

    constructor(entity: FeedEntity) : this() {
        title = entity.title
        linkUrl = entity.link.replace("#", "%23")
        content = entity.description ?: ""
        thumbnailUrl = parseThumbnailUrl(entity.contentEncoded ?: "")
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