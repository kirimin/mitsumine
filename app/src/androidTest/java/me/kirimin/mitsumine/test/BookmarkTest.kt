package me.kirimin.mitsumine.test

import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import java.util.ArrayList

import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.network.api.parser.EntryInfoApiParser
import me.kirimin.mitsumine.util.EntryInfoUtil

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat

RunWith(AndroidJUnit4::class)
public class BookmarkTest {

    Test
    public fun hasCommentはコメントが空文字じゃないかを判定() {
        val bookmark1 = Bookmark("user", ArrayList<String>(), "timestamp", "comment", "icon")
        val bookmark2 = Bookmark("user", ArrayList<String>(), "timestamp", "コメント", "icon")
        val bookmark3 = Bookmark("user", ArrayList<String>(), "timestamp", " ", "icon")
        val bookmark4 = Bookmark("user", ArrayList<String>(), "timestamp", "", "icon")
        assertThat(bookmark1.hasComment(), `is`(true))
        assertThat(bookmark2.hasComment(), `is`(true))
        assertThat(bookmark3.hasComment(), `is`(true))
        assertThat(bookmark4.hasComment(), `is`(false))
    }
}
