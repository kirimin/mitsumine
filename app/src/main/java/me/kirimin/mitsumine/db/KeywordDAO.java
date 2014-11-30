package me.kirimin.mitsumine.db;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Keyword;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

public class KeywordDAO {

    public static void save(String word) {
        Keyword keyword = new Keyword();
        keyword.word = word;
        keyword.save();
    }

    public static List<String> findAll() {
        List<Keyword> list = new Select().from(Keyword.class).execute();
        List<String> stringList = new ArrayList<>();
        for (Keyword w : list) {
            stringList.add(w.word);
        }
        return stringList;
    }

    public static void delete(String word) {
        new Delete().from(Keyword.class).where("word = ?", word).execute();
    }

    private KeywordDAO() {
    }
}
