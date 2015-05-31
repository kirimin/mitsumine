package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

RunWith(javaClass<Suite>())
Suite.SuiteClasses(javaClass<AndroidTestSuite>(), javaClass<UnitTestSuite>())
public class AllTests
