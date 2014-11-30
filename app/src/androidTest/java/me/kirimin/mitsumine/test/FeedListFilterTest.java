package me.kirimin.mitsumine.test;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.util.FeedListFilter;
import junit.framework.TestCase;

public class FeedListFilterTest extends TestCase {

    public void testContainsはリスト内に同じオブジェクトがある時にtrueを返す() {
        Feed test1 = new Feed();
        test1.title = "test1";

        Feed test2 = new Feed();
        test2.title = "test2";

        Feed test3 = new Feed();
        test2.title = "test3";

        List<Feed> list = new ArrayList<>();
        list.add(test1);
        list.add(test2);
        list.add(test3);

        assertTrue(FeedListFilter.contains(test1, list));
        assertTrue(FeedListFilter.contains(test2, list));
        assertTrue(FeedListFilter.contains(test3, list));
    }

    public void testContainsは同じリスト内に同じタイトルのフィードがある時にtrueを返す() {
        Feed test1 = new Feed();
        test1.title = "test1";

        Feed test2 = new Feed();
        test2.title = "test1";

        List<Feed> list = new ArrayList<>();
        list.add(test1);

        assertTrue(FeedListFilter.contains(test2, list));
    }

    public void testContainsは同じリスト内に同じタイトルのフィードがない時にfalseを返す() {
        Feed test1 = new Feed();
        test1.title = "test1";

        Feed test2 = new Feed();
        test2.title = "test2";

        List<Feed> list = new ArrayList<>();
        list.add(test1);

        assertFalse(FeedListFilter.contains(test2, list));
    }

    public void testContainsWordはリスト内のワードがタイトルに含まれればtrueを返す() {
        Feed test1 = new Feed();
        test1.title = "test1";
        test1.linkUrl = "testLink";

        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("testA");
        list.add("t");

        assertTrue(FeedListFilter.containsWord(test1, list));
    }

    public void testContainsWordはリスト内のワードがタイトルに含まれなければfalseを返す() {
        Feed test1 = new Feed();
        test1.title = "test1";
        test1.linkUrl = "testLink";
        test1.content = "testA";

        List<String> list = new ArrayList<>();
        list.add("tes1");
        list.add("testA");
        list.add("ta");

        assertFalse(FeedListFilter.containsWord(test1, list));
    }

    public void testContainsWordはリスト内のワードがlinkUrlに含まれればtrueを返す() {
        Feed test1 = new Feed();
        test1.title = "test1";
        test1.linkUrl = "testLink";

        List<String> list = new ArrayList<>();
        list.add("Link");
        list.add("testA");
        list.add("ta");

        assertTrue(FeedListFilter.containsWord(test1, list));
    }
}