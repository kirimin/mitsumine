package me.kirimin.mitsumine.common.database

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine.common.domain.model.UserId

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

public class UserIdDAO private constructor() {
    companion object {

        public fun save(word: String) {
            val user = UserId()
            user.word = word
            user.save()
        }

        public fun findAll(): List<String> {
            val list = Select().from(UserId::class.java).execute<UserId>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        public fun delete(word: String) {
            Delete().from(UserId::class.java).where("word = ?", word).execute<Model>()
        }
    }
}
