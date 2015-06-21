package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.EntryInfoFuncTest
import me.kirimin.mitsumine.test.FeedUtilTest

RunWith(Suite::class)
Suite.SuiteClasses(FeedUtilTest::class, EntryInfoFuncTest::class)
public class UnitTestSuite