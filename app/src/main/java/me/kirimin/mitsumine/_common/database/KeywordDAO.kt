package me.kirimin.mitsumine._common.database

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine._common.domain.model.Keyword

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

class KeywordDAO private constructor() {
    companion object {

        fun save(word: String) {
            val keyword = Keyword()
            keyword.word = word
            keyword.save()
        }

        fun findAll(): List<String> {
            val list = Select().from(Keyword::class.java).execute<Keyword>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        fun delete(word: String) {
            Delete().from(Keyword::class.java).where("word = ?", word).execute<Model>()
        }
    }
}
