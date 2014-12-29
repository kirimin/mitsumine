package me.kirimin.mitsumine.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.model.EntryInfo;
import me.kirimin.mitsumine.network.EntryInfoAccessor;
import me.kirimin.mitsumine.network.RequestQueueSingleton;

@RunWith(AndroidJUnit4.class)
public class EntryInfoAccessorTest {

    public EntryInfoAccessorTest() {
    }

    @Test
    public void requestTest() throws InterruptedException {
        final boolean[] flag = {false};
        EntryInfoAccessor.request(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new EntryInfoAccessor.EntryInfoListener() {
            @Override
            public void onSuccess(EntryInfo feedDetail) {
                flag[0] = true;
            }

            @Override
            public void onError(String errorMessage) {
                Assert.fail(errorMessage);
            }
        }, "http://kirimin.hatenablog.com/entry/20140629/1404039922");

        if (!waitForSuccess(flag)) {
            Assert.fail();
        }
    }

    private boolean waitForSuccess(boolean[] flag) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            if (flag[0]) return true;
            Thread.sleep(50);
        }
        return false;
    }
}
