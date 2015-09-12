package me.kirimin.mitsumine.test

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert

import org.junit.Test
import org.junit.runner.RunWith

import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.network.api.FeedApi.CATEGORY
import me.kirimin.mitsumine.network.api.FeedApi.TYPE
import me.kirimin.mitsumine.network.RequestQueueSingleton

import android.support.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.not

RunWith(AndroidJUnit4::class)
public class FeedApiTest {

    Test
    throws(InterruptedException::class)
    public fun requestCategoryはFeedを取得できる() {
        FeedApi.requestCategory(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), CATEGORY.MAIN, TYPE.HOT)
                .count()
                .subscribe() { count ->
                    assertThat(count, not(0))
                }
    }

    Test
    throws(InterruptedException::class)
    public fun requestKeywordはFeedを取得できる() {
        FeedApi.requestKeyword(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), "java")
                .count()
                .subscribe() { count ->
                    assertThat(count, not(0))
                }
    }

    Test
    throws(InterruptedException::class)
    public fun requestUserBookmarkはFeedを取得できる() {
        FeedApi.requestUserBookmark(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), "kirimin")
                .count()
                .subscribe() { count ->
                    assertThat(count, not(0))
                }
    }
}
