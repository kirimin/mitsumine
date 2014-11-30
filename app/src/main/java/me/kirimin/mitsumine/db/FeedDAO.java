package me.kirimin.mitsumine.db;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import me.kirimin.mitsumine.model.Feed;

public class FeedDAO {

    public static void save(Feed feed) {
        Calendar cal = Calendar.getInstance(Locale.JAPAN);
        feed.saveTime = cal.getTime().getTime();
        feed.save();
    }

    public static List<Feed> findAll() {
        return new Select().from(Feed.class).execute();
    }

    public static List<Feed> findByType(String type) {
        return new Select().from(Feed.class).where("type = ?", type).execute();
    }
    
    public static void deleteOldData(){
        Calendar cal = Calendar.getInstance(Locale.JAPAN);
        long fiveDays = 1000 * 60 * 60 * 24 * 3;
        new Delete().from(Feed.class).where("saveTime < ? AND type = ?", cal.getTime().getTime() - fiveDays, Feed.TYPE_READ).execute();
    }

    private FeedDAO() {
    }
}
