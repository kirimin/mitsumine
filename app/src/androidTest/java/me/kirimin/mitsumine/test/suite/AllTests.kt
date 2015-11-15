package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(TopTestSuite::class, EntryInfoTestSuite::class, BookmarkListTestSuite::class, FeedTestSuite::class)
public class AllTests
