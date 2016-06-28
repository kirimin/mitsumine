package me.kirimin.mitsumine._common.database

import java.util.Calendar
import java.util.Locale

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine._common.domain.model.Feed

object FeedDAO {

    fun save(model: Feed) {
        val cal = Calendar.getInstance(Locale.JAPAN)
        model.saveTime = cal.time.time
        model.save()
    }

    fun findAll(): List<Feed> {
        return Select().from(Feed::class.java).execute<Feed>()
    }

    fun findByType(type: String): List<Feed> {
        return Select().from(Feed::class.java).where("type = ?", type).execute<Feed>()
    }

    fun deleteOldData(days: Int) {
        val cal = Calendar.getInstance(Locale.JAPAN)
        val milliseconds = 1000 * 60 * 60 * 24 * days
        Delete().from(Feed::class.java).where("saveTime < ? AND type = ?", cal.time.time - milliseconds, Feed.TYPE_READ).execute<Feed>()
    }

}
