package me.kirimin.mitsumine.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import me.kirimin.mitsumine.test.BookmarkFeedAccessorTest;
import me.kirimin.mitsumine.test.FeedListFilterTest;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(AndroidTestSuite.class);
        suite.addTestSuite(UnitTestSuite.class);
        return suite;
    }
}
