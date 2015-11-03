package me.kirimin.mitsumine.test.suite

import me.kirimin.mitsumine.test.entryinfo.EntryInfoUseCaseTest
import me.kirimin.mitsumine.test.entryinfo.EntryInfoPresenterTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(EntryInfoPresenterTest::class, EntryInfoUseCaseTest::class)
class EntryInfoTestSuite {
}