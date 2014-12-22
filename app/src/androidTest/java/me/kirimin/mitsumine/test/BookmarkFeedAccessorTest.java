package me.kirimin.mitsumine.test;

import java.util.List;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.FeedListener;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.activity.TopActivity;

@RunWith(AndroidJUnit4.class)
public class BookmarkFeedAccessorTest extends ActivityInstrumentationTestCase2<TopActivity> {

    private TopActivity activity;

    public BookmarkFeedAccessorTest() {
        super(TopActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void requestCategoryはFeedを取得できる() {
        BookmarkFeedAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(activity.getApplicationContext()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
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
        BookmarkFeedAccessor.requestKeyword(RequestQueueSingleton.getRequestQueue(activity), new FeedListener() {

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
        BookmarkFeedAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(activity), new FeedListener() {

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
