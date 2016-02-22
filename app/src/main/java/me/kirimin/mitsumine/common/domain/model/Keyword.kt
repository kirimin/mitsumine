package me.kirimin.mitsumine.common.domain.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "keyword")
public class Keyword : Model() {

    @Column(name = "word", unique = true)
    public var word: String = ""
}
