package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.EntryInfoApiTest
import me.kirimin.mitsumine.test.FeedApiTest

RunWith(javaClass<Suite>())
Suite.SuiteClasses(javaClass<FeedApiTest>(), javaClass<EntryInfoApiTest>())
public class AndroidTestSuite