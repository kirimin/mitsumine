package me.kirimin.mitsumine.db

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine.model.NGWord

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

public class NGWordDAO private() {
    companion object {

        public fun save(word: String) {
            val ngWord = NGWord()
            ngWord.word = word
            ngWord.save()
        }

        public fun findAll(): List<String> {
            val list = Select().from(javaClass<NGWord>()).execute<NGWord>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        public fun delete(word: String) {
            Delete().from(javaClass<NGWord>()).where("word = ?", word).execute<Model>()
        }
    }
}
