package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.EntryInfoUtilTest
import me.kirimin.mitsumine.test.FeedUtilTest

RunWith(Suite::class)
Suite.SuiteClasses(FeedUtilTest::class, EntryInfoUtilTest::class)
public class UnitTestSuite