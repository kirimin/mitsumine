package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

RunWith(Suite::class)
Suite.SuiteClasses(AndroidTestSuite::class, UnitTestSuite::class)
public class AllTests
