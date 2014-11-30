package me.kirimin.mitsumine.db;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.NGWord;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

public class NGWordDAO {

    public static void save(String word) {
        NGWord ngWord = new NGWord();
        ngWord.word = word;
        ngWord.save();
    }

    public static List<String> findAll() {
        List<NGWord> list = new Select().from(NGWord.class).execute();
        List<String> stringList = new ArrayList<>();
        for (NGWord w : list) {
            stringList.add(w.word);
        }
        return stringList;
    }

    public static void delete(String word) {
        new Delete().from(NGWord.class).where("word = ?", word).execute();
    }

    private NGWordDAO() {
    }
}
