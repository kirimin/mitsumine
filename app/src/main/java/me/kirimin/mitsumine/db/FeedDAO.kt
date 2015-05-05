package me.kirimin.mitsumine.db

import com.activeandroid.Model
import java.util.Calendar
import java.util.Locale

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine.model.Feed

public class FeedDAO private() {
    companion object {

        public fun save(feed: Feed) {
            val cal = Calendar.getInstance(Locale.JAPAN)
            feed.saveTime = cal.getTime().getTime()
            feed.save()
        }

        public fun findAll(): List<Feed> {
            return Select().from(javaClass<Feed>()).execute<Feed>()
        }

        public fun findByType(type: String): List<Feed> {
            return Select().from(javaClass<Feed>()).where("type = ?", type).execute<Feed>()
        }

        public fun deleteOldData() {
            val cal = Calendar.getInstance(Locale.JAPAN)
            val fiveDays = 1000 * 60 * 60 * 24 * 3.toLong()
            Delete().from(javaClass<Feed>()).where("saveTime < ? AND type = ?", cal.getTime().getTime() - fiveDays, Feed.TYPE_READ).execute<Model>()
        }
    }
}
