package me.kirimin.mitsumine.test;

import java.util.List;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Toast;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.CATEGORY;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.FeedListener;
import me.kirimin.mitsumine.network.BookmarkFeedAccessor.TYPE;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.activity.TopActivity;

public class BookmarkFeedAccessorTest extends ActivityInstrumentationTestCase2<TopActivity> {

    Solo solo;

    public BookmarkFeedAccessorTest() {
        super(TopActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testRequestCategoryはFeedを取得できる() {
        BookmarkFeedAccessor.requestCategory(RequestQueueSingleton.getRequestQueue(getActivity()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                assertTrue(false);
            }
        }, CATEGORY.MAIN, TYPE.HOT);
        solo.waitForText("success");
    }

    public void testRequestKeywordはFeedを取得できる() {
        BookmarkFeedAccessor.requestKeyword(RequestQueueSingleton.getRequestQueue(getActivity()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                assertTrue(false);
            }
        }, "Java");
        solo.waitForText("success");
    }

    public void testRequestUserBookmarkはFeedを取得できる() {
        BookmarkFeedAccessor.requestUserBookmark(RequestQueueSingleton.getRequestQueue(getActivity()), new FeedListener() {

            @Override
            public void onSuccess(List<Feed> feedList) {
                if (!feedList.isEmpty()) {
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                assertTrue(false);
            }
        }, "hajimepg");
        solo.waitForText("success");
    }
}
