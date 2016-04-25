package me.kirimin.mitsumine.common.domain.model

import me.kirimin.mitsumine.common.network.entity.Item

class Feed(var title: String = "",
           var thumbnailUrl: String = "",
           var content: String = "",
           var linkUrl: String = "",
           var entryLinkUrl: String = "",
           var bookmarkCountUrl: String = "",
           var faviconUrl: String = "",
           var type: String = "",
           var saveTime: Long = 0) {

    constructor(apiData: Item) : this() {
        title = apiData.title
        linkUrl = apiData.link.replace("#", "%23")
        content = apiData.description
        apiData.contentEncoded?.let { thumbnailUrl = parseThumbnailUrl(it) }
        bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + linkUrl
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