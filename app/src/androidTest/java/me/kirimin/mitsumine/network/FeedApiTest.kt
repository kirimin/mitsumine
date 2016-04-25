package me.kirimin.mitsumine.network

import android.support.test.InstrumentationRegistry

import org.junit.Test

import me.kirimin.mitsumine._common.network.FeedApi
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type

import org.hamcrest.Matchers.not
import org.junit.Assert.*;
import rx.observers.TestSubscriber

public class FeedApiTest {

    @Test
    public fun requestCategoryはFeedを取得できる() {
        val testSubscriber = TestSubscriber<Int>()
        FeedApi.requestCategory(InstrumentationRegistry.getContext(), Category.MAIN, Type.HOT)
                .count()
                .subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        assertThat(testSubscriber.onNextEvents.get(0), not(0))
    }

    @Test
    public fun requestKeywordはFeedを取得できる() {
        val testSubscriber = TestSubscriber<Int>()
        FeedApi.requestKeyword(InstrumentationRegistry.getContext(), "java")
                .count()
                .subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        assertThat(testSubscriber.onNextEvents.get(0), not(0))

    }

    @Test
    public fun requestUserBookmarkはFeedを取得できる() {
        val testSubscriber = TestSubscriber<Int>()
        FeedApi.requestUserBookmark(InstrumentationRegistry.getContext(), "kirimin")
                .count()
                .subscribe(testSubscriber)
        testSubscriber.assertNoErrors()
        assertThat(testSubscriber.onNextEvents.get(0), not(0))

    }
}
