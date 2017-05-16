package me.kirimin.mitsumine._common.domain.model

import com.nhaarman.mockito_kotlin.whenever
import me.kirimin.mitsumine.BuildConfig
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse
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
class EntryInfoTest {

    @Rule
    @JvmField
    var mockito = MockitoJUnit.rule()

    @Mock
    lateinit var entryInfoResponse: EntryInfoResponse
    lateinit var entryInfo: EntryInfo

    @Before
    fun setUp() {
        entryInfo = EntryInfo(entryInfoResponse)
    }

    @Test
    fun getTagListTest() {
        val bookmarks = listOf(
                BookmarkResponse(tags = listOf("A", "B", "C", "D", "E")),
                BookmarkResponse(tags = listOf("B", "C", "D", "E")),
                BookmarkResponse(tags = listOf("C", "D", "E")),
                BookmarkResponse(tags = listOf("D", "E")),
                BookmarkResponse(tags = listOf("E")),
                BookmarkResponse(tags = emptyList())
        )
        whenever(entryInfoResponse.bookmarks).thenReturn(bookmarks)
        assertThat(entryInfo.tagListString, `is`("E, D, C, B"))
    }
}