package me.kirimin.mitsumine.test

import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import java.util.ArrayList

import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.util.FeedUtil

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
public class FeedUtilTest {

    @Test
    public fun containsはリスト内に同じタイトルのFeedが無いかを判定する() {
        val test1 = Feed()
        test1.title = "test1"

        val test2 = Feed()
        test2.title = "test2"

        val test3 = Feed()
        test3.title = "test3"

        val test4 = Feed()
        test4.title = "test1"

        val list = ArrayList<Feed>()
        list.add(test1)
        list.add(test3)

        assertThat(!FeedUtil.contains(test1, list), `is`(false))
        assertThat(!FeedUtil.contains(test2, list), `is`(true))
        assertThat(!FeedUtil.contains(test3, list), `is`(false))
        assertThat(!FeedUtil.contains(test4, list), `is`(false))
    }

    @Test
    public fun containsWordはリスト内の文字列とタイトルが一致しないかを判定する() {
        val list = ArrayList<String>()
        list.add("tes1")
        list.add("testA")
        list.add("ta")

        val test1 = Feed()
        test1.title = "test1"
        test1.linkUrl = "testLink"
        test1.content = "testA"

        val test2 = Feed()
        test2.title = "testA"
        test2.linkUrl = "testLink"
        test2.content = "testA"

        val test3 = Feed()
        test3.title = "testTitle"
        test3.linkUrl = "testA"
        test3.content = "testContent"

        assertThat(!FeedUtil.containsWord(test1, list), `is`(true))
        assertThat(!FeedUtil.containsWord(test2, list), `is`(false))
        assertThat(!FeedUtil.containsWord(test3, list), `is`(false))
    }
}