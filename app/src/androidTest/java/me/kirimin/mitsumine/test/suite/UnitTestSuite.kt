package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.EntryInfoFuncTest
import me.kirimin.mitsumine.test.FeedFuncTest

RunWith(Suite::class)
Suite.SuiteClasses(FeedFuncTest::class, EntryInfoFuncTest::class)
public class UnitTestSuite