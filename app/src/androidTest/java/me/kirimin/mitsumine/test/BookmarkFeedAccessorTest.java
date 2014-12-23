package me.kirimin.mitsumine.test;

import java.util.List;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.FeedListener;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;

@RunWith(AndroidJUnit4.class)
public class BookmarkFeedAccessorTest {

    public BookmarkFeedAccessorTest() {
    }

    @Test
    public void requestCategoryはFeedを取得できる() throws InterruptedException {
        final boolean[] flag = {false};
        BookmarkFeedAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                flag[0] = true;
            }

            @Override
            public void onError(String errorMessage) {
                Assert.fail();
            }
        }, CATEGORY.MAIN, TYPE.HOT);

        if (!waitForSuccess(flag)) {
            Assert.fail();
        }
    }

    @Test
    public void requestKeywordはFeedを取得できる() throws InterruptedException {
        final boolean[] flag = {false};
        BookmarkFeedAccessor.requestKeyword(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    flag[0] = true;
                } else {
                    Assert.fail();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Assert.fail();
            }
        }, "Java");

        if (!waitForSuccess(flag)) {
            Assert.fail();
        }
    }

    @Test
    public void requestUserBookmarkはFeedを取得できる() throws InterruptedException {
        final boolean[] flag = {false};
        BookmarkFeedAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    flag[0] = true;
                } else {
                    Assert.fail();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Assert.fail();
            }
        }, "hajimepg");

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
