package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.FeedEntity
import java.net.URLEncoder

/**
 * はてブのエントリフィードのモデルクラス
 */
data class Feed(
        var title: String = "",
        var thumbnailUrl: String = "",
        var content: String = "",
        var linkUrl: String = "", // ブックマークのURL
        var entryLinkUrl: String = "", // エントリのURL
        var bookmarkCountUrl: String = "", // ブクマ数取得のURL
        var faviconUrl: String = "", // faviconのURL
        var type: String = "", // あとで読む・既読の状態
        var saveTime: Long = 0) {

    constructor(entity: FeedEntity) : this() {
        title = entity.title
        thumbnailUrl = parseThumbnailUrl(entity.contentEncoded ?: "")
        content = entity.description ?: ""
        linkUrl = entity.link.replace("#", "%23")
        bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + URLEncoder.encode(linkUrl, "utf-8")
        faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + linkUrl
        entryLinkUrl = "http://b.hatena.ne.jp/entry/" + linkUrl
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