package me.kirimin.mitsumine._common.database

import com.activeandroid.Model
import java.util.ArrayList

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import me.kirimin.mitsumine._common.domain.model.NGWord

class NGWordDAO private constructor() {
    companion object {

        fun save(word: String) {
            val ngWord = NGWord()
            ngWord.word = word
            ngWord.save()
        }

        fun findAll(): List<String> {
            val list = Select().from(NGWord::class.java).execute<NGWord>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word!!)
            }
            return stringList
        }

        fun delete(word: String) {
            Delete().from(NGWord::class.java).where("word = ?", word).execute<Model>()
        }
    }
}
