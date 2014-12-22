package me.kirimin.mitsumine.test;

import java.util.List;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.FeedListener;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BookmarkFeedAccessorTest {

    public BookmarkFeedAccessorTest() {
    }

    @Test
    public void requestCategoryはFeedを取得できる() {
        BookmarkFeedAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    assertTrue(true);
                }
            }

            @Override
            public void onError(String errorMessage) {
                assertTrue(false);
            }
        }, CATEGORY.MAIN, TYPE.HOT);
    }

    @Test
    public void requestKeywordはFeedを取得できる() {
        BookmarkFeedAccessor.requestKeyword(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    assertTrue(true);
                }
            }

            @Override
            public void onError(String errorMessage) {
                assertTrue(false);
            }
        }, "Java");
    }

    @Test
    public void requestUserBookmarkはFeedを取得できる() {
        BookmarkFeedAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(InstrumentationRegistry.getContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    assertTrue(true);
                }
            }

            @Override
            public void onError(String errorMessage) {
                assertTrue(false);
            }
        }, "hajimepg");
    }
}
