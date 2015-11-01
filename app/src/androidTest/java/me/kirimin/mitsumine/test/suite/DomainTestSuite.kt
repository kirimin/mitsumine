package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.domain.FeedUtilTest
import me.kirimin.mitsumine.test.model.BookmarkTest

@RunWith(Suite::class)
@Suite.SuiteClasses(FeedUtilTest::class, BookmarkTest::class)
public class DomainTestSuite