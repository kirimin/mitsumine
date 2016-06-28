package me.kirimin.mitsumine._common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "userid")
class UserId : Model() {

    @Column(name = "word", unique = true)
    var word: String = ""
}
