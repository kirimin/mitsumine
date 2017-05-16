package me.kirimin.mitsumine._common.domain.model

import com.nhaarman.mockito_kotlin.whenever
import me.kirimin.mitsumine.BuildConfig
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class BookmarkTest {

    @Rule
    @JvmField
    var mockito = MockitoJUnit.rule()

    @Mock
    lateinit var bookmarkResponse: BookmarkResponse
    lateinit var bookmark: Bookmark

    @Before
    fun setUp() {
        bookmark = Bookmark(bookmarkResponse)
    }

    @Test
    fun getUserIconTest() {
        whenever(bookmarkResponse.user).thenReturn("userId")
        assertThat(bookmark.userIcon, `is`("http://cdn1.www.st-hatena.com/users/us/userId/profile.gif"))
    }

    @Test
    fun getTimestampTest() {
        whenever(bookmarkResponse.timestamp).thenReturn("2017/02/01 08:30")
        assertThat(bookmark.timestamp, `is`("2017/02/01"))
    }
}