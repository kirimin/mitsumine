package me.kirimin.mitsumine.common.database

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.Calendar
import java.util.Locale

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine.common.domain.model.Feed

object FeedDAO {

    fun save(feed: Feed) {
        val model = FeedDBModel(feed)
        val cal = Calendar.getInstance(Locale.JAPAN)
        model.saveTime = cal.time.time
        model.save()
    }

    fun findAll(): List<Feed> {
        return Select().from(FeedDBModel::class.java).execute<FeedDBModel>().map { it.toFeed() }
    }

    fun findByType(type: String): List<Feed> {
        return Select().from(FeedDBModel::class.java).where("type = ?", type).execute<FeedDBModel>().map { it.toFeed() }
    }

    fun deleteOldData(days: Int) {
        val cal = Calendar.getInstance(Locale.JAPAN)
        val milliseconds = 1000 * 60 * 60 * 24 * days
        Delete().from(FeedDBModel::class.java).where("saveTime < ? AND type = ?", cal.time.time - milliseconds, Feed.TYPE_READ).execute<FeedDBModel>()
    }

    @Table(name = "feed")
    private class FeedDBModel(feed: Feed) : Model() {

        constructor() : this(Feed())

        @Column(name = "title")
        var title: String = feed.title

        @Column(name = "thumbnailUrl")
        var thumbnailUrl: String = feed.thumbnailUrl

        @Column(name = "content")
        var content: String = feed.content

        @Column(name = "linkUrl", unique = true)
        var linkUrl: String = feed.linkUrl

        @Column(name = "entryLinkUrl")
        var entryLinkUrl: String = feed.entryLinkUrl

        @Column(name = "bookmarkCountUrl")
        var bookmarkCountUrl: String = feed.bookmarkCountUrl

        @Column(name = "faviconUrl")
        var faviconUrl: String = feed.faviconUrl

        @Column(name = "type")
        var type: String = feed.type

        @Column(name = "saveTime")
        var saveTime: Long = feed.saveTime

        fun toFeed(): Feed {
            val feed = Feed()
            feed.title = title
            feed.thumbnailUrl = thumbnailUrl
            feed.content = content
            feed.linkUrl = linkUrl
            feed.entryLinkUrl = entryLinkUrl
            feed.bookmarkCountUrl = bookmarkCountUrl
            feed.faviconUrl = faviconUrl
            feed.type = type
            feed.saveTime = saveTime
            return feed
        }
    }
}
