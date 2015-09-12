package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.EntryInfoApiTest
import me.kirimin.mitsumine.test.FeedApiTest

RunWith(Suite::class)
Suite.SuiteClasses(FeedApiTest::class, EntryInfoApiTest::class)
public class AndroidTestSuite