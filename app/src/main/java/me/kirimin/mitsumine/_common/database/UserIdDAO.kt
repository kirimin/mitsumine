package me.kirimin.mitsumine._common.database

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine._common.domain.model.UserId

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

class UserIdDAO private constructor() {
    companion object {

        fun save(word: String) {
            val user = UserId()
            user.word = word
            user.save()
        }

        fun findAll(): List<String> {
            val list = Select().from(UserId::class.java).execute<UserId>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        fun delete(word: String) {
            Delete().from(UserId::class.java).where("word = ?", word).execute<Model>()
        }
    }
}
