package me.kirimin.mitsumine.test

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.network.api.FeedApi.CATEGORY
import me.kirimin.mitsumine.network.api.FeedApi.TYPE

import android.support.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.not

@RunWith(AndroidJUnit4::class)
public class FeedApiTest {

    @Test
    public fun requestCategoryはFeedを取得できる() {
        FeedApi.requestCategory(CATEGORY.MAIN, TYPE.HOT)
                .count()
                .subscribe() { count ->
                    assertThat(count, not(0))
                }
    }

    @Test
    public fun requestKeywordはFeedを取得できる() {
        FeedApi.requestKeyword("java")
                .count()
                .subscribe() { count ->
                    assertThat(count, not(0))
                }
    }

    @Test
    public fun requestUserBookmarkはFeedを取得できる() {
        FeedApi.requestUserBookmark("kirimin")
                .count()
                .subscribe() { count ->
                    assertThat(count, not(0))
                }
    }
}
