package me.kirimin.mitsumine.test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import me.kirimin.mitsumine.test.FeedListFilterTest;

public class UnitTestSuite extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(FeedListFilterTest.class);
        return suite;
    }
}
