package me.kirimin.mitsumine.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import me.kirimin.mitsumine.test.EntryInfoApiAccessorTest;
import me.kirimin.mitsumine.test.FeedApiAccessorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({FeedApiAccessorTest.class, EntryInfoApiAccessorTest.class})
public class AndroidTestSuite {
}