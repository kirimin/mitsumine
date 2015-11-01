package me.kirimin.mitsumine.test.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(DataTestSuite::class, DomainTestSuite::class, PresenterTestSuite::class)
public class AllTests
