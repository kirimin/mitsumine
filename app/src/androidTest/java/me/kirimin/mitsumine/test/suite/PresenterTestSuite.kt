package me.kirimin.mitsumine.test.suite

import me.kirimin.mitsumine.test.presenter.EntryInfoPresenterTest
import me.kirimin.mitsumine.test.presenter.TopPresenterTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(TopPresenterTest::class, EntryInfoPresenterTest::class)
class PresenterTestSuite