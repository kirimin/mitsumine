package me.kirimin.mitsumine.db

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine.model.Keyword

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

public class KeywordDAO private() {
    companion object {

        public fun save(word: String) {
            val keyword = Keyword()
            keyword.word = word
            keyword.save()
        }

        public fun findAll(): List<String> {
            val list = Select().from(javaClass<Keyword>()).execute<Keyword>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        public fun delete(word: String) {
            Delete().from(javaClass<Keyword>()).where("word = ?", word).execute<Model>()
        }
    }
}
