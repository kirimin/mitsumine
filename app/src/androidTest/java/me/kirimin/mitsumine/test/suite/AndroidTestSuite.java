package me.kirimin.mitsumine.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import me.kirimin.mitsumine.test.BookmarkFeedAccessorTest;
import me.kirimin.mitsumine.test.EntryInfoAccessorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({BookmarkFeedAccessorTest.class, EntryInfoAccessorTest.class})
public class AndroidTestSuite {
}