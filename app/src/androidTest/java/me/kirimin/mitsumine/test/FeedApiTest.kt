package me.kirimin.mitsumine.test

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert

import org.json.JSONException
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith

import me.kirimin.mitsumine.network.api.FeedApi
import me.kirimin.mitsumine.network.api.FeedApi.CATEGORY
import me.kirimin.mitsumine.network.api.FeedApi.TYPE
import me.kirimin.mitsumine.network.RequestQueueSingleton
import rx.functions.Action1

import android.support.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.not

RunWith(AndroidJUnit4::class)
public class FeedApiTest {

    Test
    throws(javaClass<InterruptedException>())
    public fun requestCategoryはFeedを取得できる() {
        FeedApi.requestCategory(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), CATEGORY.MAIN, TYPE.HOT).subscribe(object : Action1<JSONObject> {
            override fun call(jsonObject: JSONObject) {
                try {
                    assertThat(jsonObject.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries").length(), not(0))
                } catch (e: JSONException) {
                    Assert.fail()
                }

            }
        })
    }

    Test
    throws(javaClass<InterruptedException>())
    public fun requestKeywordはFeedを取得できる() {
        FeedApi.requestKeyword(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), "java").subscribe(object : Action1<JSONObject> {
            override fun call(jsonObject: JSONObject) {
                try {
                    assertThat(jsonObject.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries").length(), not(0))
                } catch (e: JSONException) {
                    Assert.fail()
                }

            }
        })
    }

    Test
    throws(javaClass<InterruptedException>())
    public fun requestUserBookmarkはFeedを取得できる() {
        FeedApi.requestUserBookmark(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), "hajimepg").subscribe(object : Action1<JSONObject> {
            override fun call(jsonObject: JSONObject) {
                try {
                    assertThat(jsonObject.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries").length(), not(0))
                } catch (e: JSONException) {
                    Assert.fail()
                }

            }
        })
    }
}
