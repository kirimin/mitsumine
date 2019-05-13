package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.domain.extensions.normalizeToNFC
import me.kirimin.mitsumine._common.network.entity.FeedResponse
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

    constructor(response: FeedResponse) : this() {
        title = response.title.normalizeToNFC()
        thumbnailUrl = parseThumbnailUrl(response.contentEncoded ?: "")
        content = response.description?.normalizeToNFC() ?: ""
        linkUrl = response.link.replace("#", "%23")
        bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + URLEncoder.encode(linkUrl, "utf-8")
        faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + linkUrl
        entryLinkUrl = "http://b.hatena.ne.jp/entry/" + linkUrl
    }

    private fun parseThumbnailUrl(content: String): String {
        val urlStartIndex = content.indexOf("https://cdn-ak-scissors.b.st-hatena.com/image/square/")
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