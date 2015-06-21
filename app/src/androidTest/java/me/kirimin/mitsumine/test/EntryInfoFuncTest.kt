package me.kirimin.mitsumine.test

import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import java.util.ArrayList

import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.network.api.parser.EntryInfoApiParser

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat

RunWith(AndroidJUnit4::class)
public class EntryInfoFuncTest {

    Test
    public fun hasCommentはコメントが空文字じゃないかを判定() {
        val bookmark1 = Bookmark("user", ArrayList<String>(), "timestamp", "comment", "icon")
        val bookmark2 = Bookmark("user", ArrayList<String>(), "timestamp", "コメント", "icon")
        val bookmark3 = Bookmark("user", ArrayList<String>(), "timestamp", " ", "icon")
        val bookmark4 = Bookmark("user", ArrayList<String>(), "timestamp", "", "icon")
        assertThat(EntryInfoApiParser.hasComment(bookmark1), `is`(true))
        assertThat(EntryInfoApiParser.hasComment(bookmark2), `is`(true))
        assertThat(EntryInfoApiParser.hasComment(bookmark3), `is`(true))
        assertThat(EntryInfoApiParser.hasComment(bookmark4), `is`(false))
    }
}
