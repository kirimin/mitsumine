package me.kirimin.mitsumine.common.database

import com.activeandroid.Model
import java.util.Calendar
import java.util.Locale

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine.common.domain.model.Feed

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

        public fun deleteOldData(days: Int) {
            val cal = Calendar.getInstance(Locale.JAPAN)
            val milliseconds = 1000 * 60 * 60 * 24 * days
            Delete().from(Feed::class.java).where("saveTime < ? AND type = ?", cal.time.time - milliseconds, Feed.TYPE_READ).execute<Model>()
        }
    }
}
