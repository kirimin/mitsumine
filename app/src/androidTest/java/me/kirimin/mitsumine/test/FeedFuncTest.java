package me.kirimin.mitsumine.test;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.util.FeedFunc;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class FeedFuncTest {

    @Test
    public void notContainsはリスト内に同じタイトルのFeedが無いかを判定する() {
        Feed test1 = new Feed();
        test1.title = "test1";

        Feed test2 = new Feed();
        test2.title = "test2";

        Feed test3 = new Feed();
        test3.title = "test3";

        Feed test4 = new Feed();
        test4.title = "test1";

        List<Feed> list = new ArrayList<>();
        list.add(test1);
        list.add(test3);

        assertThat(!FeedFunc.Companion.contains(test1, list), is(false));
        assertThat(!FeedFunc.Companion.contains(test2, list), is(true));
        assertThat(!FeedFunc.Companion.contains(test3, list), is(false));
        assertThat(!FeedFunc.Companion.contains(test4, list), is(false));
    }

    @Test
    public void notContainsWordはリスト内の文字列とタイトルが一致しないかを判定する() {
        List<String> list = new ArrayList<>();
        list.add("tes1");
        list.add("testA");
        list.add("ta");

        Feed test1 = new Feed();
        test1.title = "test1";
        test1.linkUrl = "testLink";
        test1.content = "testA";

        Feed test2 = new Feed();
        test2.title = "testA";
        test2.linkUrl = "testLink";
        test2.content = "testA";

        Feed test3 = new Feed();
        test3.title = "testTitle";
        test3.linkUrl = "testA";
        test3.content = "testContent";

        assertThat(!FeedFunc.Companion.containsWord(test1, list), is(true));
        assertThat(!FeedFunc.Companion.containsWord(test2, list), is(false));
        assertThat(!FeedFunc.Companion.containsWord(test3, list), is(false));
    }
}