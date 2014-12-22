package me.kirimin.mitsumine.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import me.kirimin.mitsumine.test.BookmarkFeedAccessorTest;
import me.kirimin.mitsumine.test.FeedListFilterTest;

public class AndroidTestSuite extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(BookmarkFeedAccessorTest.class);
        return suite;

    }
}
