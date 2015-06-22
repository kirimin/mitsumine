package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.BookmarkTest
import me.kirimin.mitsumine.test.FeedUtilTest

RunWith(Suite::class)
Suite.SuiteClasses(FeedUtilTest::class, BookmarkTest::class)
public class UnitTestSuite