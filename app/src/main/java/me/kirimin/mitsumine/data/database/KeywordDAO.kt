package me.kirimin.mitsumine.data.database

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine.model.Keyword

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

public class KeywordDAO private constructor() {
    companion object {

        public fun save(word: String) {
            val keyword = Keyword()
            keyword.word = word
            keyword.save()
        }

        public fun findAll(): List<String> {
            val list = Select().from(Keyword::class.java).execute<Keyword>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        public fun delete(word: String) {
            Delete().from(Keyword::class.java).where("word = ?", word).execute<Model>()
        }
    }
}
