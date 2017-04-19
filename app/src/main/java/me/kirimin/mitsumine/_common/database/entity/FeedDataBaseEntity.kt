package me.kirimin.mitsumine._common.database.entity;

import com.activeandroid.Model
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import me.kirimin.mitsumine._common.domain.model.Feed

/**
 * フィードをDBに保存するためのテーブルモデル
 */
@Table(name = "feed")
class FeedDataBaseEntity() : Model() {

    constructor(feed: Feed): this() {
        title = feed.title
        thumbnailUrl = feed.thumbnailUrl
        content = feed.content
        linkUrl = feed.linkUrl
        entryLinkUrl = feed.entryLinkUrl
        bookmarkCountUrl = feed.bookmarkCountUrl
        faviconUrl = feed.faviconUrl
        type = feed.type
        saveTime = feed.saveTime
    }

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

    fun toModel() = Feed(title = title, thumbnailUrl = thumbnailUrl, content = content, linkUrl = linkUrl, entryLinkUrl = entryLinkUrl, bookmarkCountUrl = bookmarkCountUrl, faviconUrl = faviconUrl, type = type, saveTime = saveTime)
}
