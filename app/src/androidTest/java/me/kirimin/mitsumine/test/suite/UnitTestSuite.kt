package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

import me.kirimin.mitsumine.test.EntryInfoFuncTest
import me.kirimin.mitsumine.test.FeedFuncTest

RunWith(javaClass<Suite>())
Suite.SuiteClasses(javaClass<FeedFuncTest>(), javaClass<EntryInfoFuncTest>())
public class UnitTestSuite