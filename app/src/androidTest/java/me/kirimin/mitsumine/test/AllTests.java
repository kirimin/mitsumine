package me.kirimin.mitsumine.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(FeedListFilterTest.class);
        suite.addTestSuite(BookmarkFeedAccessorTest.class);
        return suite;

    }
}
