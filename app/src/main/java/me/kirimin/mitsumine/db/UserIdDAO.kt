package me.kirimin.mitsumine.db

import com.activeandroid.Model
import java.util.ArrayList

import me.kirimin.mitsumine.model.UserId

import com.activeandroid.query.Delete
import com.activeandroid.query.Select

public class UserIdDAO private() {
    companion object {

        public fun save(word: String) {
            val user = UserId()
            user.word = word
            user.save()
        }

        public fun findAll(): List<String> {
            val list = Select().from(javaClass<UserId>()).execute<UserId>()
            val stringList = ArrayList<String>()
            for (w in list) {
                stringList.add(w.word)
            }
            return stringList
        }

        public fun delete(word: String) {
            Delete().from(javaClass<UserId>()).where("word = ?", word).execute<Model>()
        }
    }
}
