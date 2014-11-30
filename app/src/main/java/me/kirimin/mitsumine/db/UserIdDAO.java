package me.kirimin.mitsumine.db;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.UserId;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

public class UserIdDAO {

    public static void save(String word) {
        UserId user = new UserId();
        user.word = word;
        user.save();
    }

    public static List<String> findAll() {
        List<UserId> list = new Select().from(UserId.class).execute();
        List<String> stringList = new ArrayList<>();
        for (UserId w : list) {
            stringList.add(w.word);
        }
        return stringList;
    }

    public static void delete(String word) {
        new Delete().from(UserId.class).where("word = ?", word).execute();
    }

    private UserIdDAO() {
    }
}
