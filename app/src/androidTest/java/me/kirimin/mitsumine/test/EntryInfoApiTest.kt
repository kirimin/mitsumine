package me.kirimin.mitsumine.test

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import junit.framework.Assert

import org.json.JSONException
import org.junit.Test
import org.junit.runner.RunWith

import me.kirimin.mitsumine.network.api.EntryInfoApi
import me.kirimin.mitsumine.network.RequestQueueSingleton

import android.support.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.`is`

@RunWith(AndroidJUnit4::class)
public class EntryInfoApiTest {

    @Test
    public fun requestTest() {
        val url = "http://kirimin.hatenablog.com/entry/20140629/1404039922"
        EntryInfoApi.request(RequestQueueSingleton.get(InstrumentationRegistry.getContext()), url).subscribe { bookmark ->
            try {
                assertThat(bookmark.url, `is`(url))
            } catch (e: JSONException) {
                e.printStackTrace()
                Assert.fail()
            }

        }
    }
}
