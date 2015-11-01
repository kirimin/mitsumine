package me.kirimin.mitsumine.data.database

import com.activeandroid.Model
import java.util.Calendar
import java.util.Locale

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine.model.Feed

public class FeedDAO private constructor() {
    companion object {

        public fun save(feed: Feed) {
            val cal = Calendar.getInstance(Locale.JAPAN)
            feed.saveTime = cal.time.time
            feed.save()
        }

        public fun findAll(): List<Feed> {
            return Select().from(Feed::class.java).execute<Feed>()
        }

        public fun findByType(type: String): List<Feed> {
            return Select().from(Feed::class.java).where("type = ?", type).execute<Feed>()
        }

        public fun deleteOldData(millisecond: Long) {
            val cal = Calendar.getInstance(Locale.JAPAN)
            Delete().from(Feed::class.java).where("saveTime < ? AND type = ?", cal.time.time - millisecond, Feed.TYPE_READ).execute<Model>()
        }
    }
}
