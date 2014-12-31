package me.kirimin.mitsumine.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.network.FeedApiAccessor;
import me.kirimin.mitsumine.network.FeedApiAccessor.CATEGORY;
import me.kirimin.mitsumine.network.FeedApiAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import rx.functions.Action1;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FeedApiAccessorTest {

    public FeedApiAccessorTest() {
    }

    @Test
    public void requestCategoryはFeedを取得できる() throws InterruptedException {
        FeedApiAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), CATEGORY.MAIN, TYPE.HOT)
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        try {
                            assertThat(jsonObject.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries").length(), not(0));
                        } catch (JSONException e) {
                            Assert.fail();
                        }
                    }
                });
    }

    @Test
    public void requestKeywordはFeedを取得できる() throws InterruptedException {
        FeedApiAccessor.requestKeyword(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), "java")
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        try {
                            assertThat(jsonObject.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries").length(), not(0));
                        } catch (JSONException e) {
                            Assert.fail();
                        }
                    }
                });
    }

    @Test
    public void requestUserBookmarkはFeedを取得できる() throws InterruptedException {
        FeedApiAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), "hajimepg")
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        try {
                            assertThat(jsonObject.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries").length(), not(0));
                        } catch (JSONException e) {
                            Assert.fail();
                        }
                    }
                });
    }
}
