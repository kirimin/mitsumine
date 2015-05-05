package me.kirimin.mitsumine.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.network.ApiRequestException;
import me.kirimin.mitsumine.network.api.EntryInfoApi;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import rx.functions.Action1;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class EntryInfoApiTest {

    public EntryInfoApiTest() {
    }

    @Test
    public void requestTest() throws InterruptedException, ApiRequestException {
        final String url = "http://kirimin.hatenablog.com/entry/20140629/1404039922";
        EntryInfoApi.Companion.request(RequestQueueSingleton.Companion.get(InstrumentationRegistry.getContext()), url)
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        try {
                            assertThat(jsonObject.getString("url"), is(url));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Assert.fail();
                        }
                    }
                });
    }
}
