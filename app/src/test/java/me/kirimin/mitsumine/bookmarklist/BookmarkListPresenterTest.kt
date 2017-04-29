package me.kirimin.mitsumine.bookmarklist

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import me.kirimin.mitsumine.BuildConfig
import org.junit.Test

import java.util.ArrayList

import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class BookmarkListPresenterTest {

    @Rule
    @JvmField
    var mockito = MockitoJUnit.rule()

    @Mock
    lateinit var viewMock: BookmarkListView
    @Spy
    @InjectMocks
    lateinit var presenter: BookmarkListPresenter

    @Test
    fun onCreateTest() {
        val bookmarks = ArrayList<Bookmark>()
        presenter.onCreate(viewMock, bookmarks)
        verify(viewMock, times(1)).initViews(bookmarks)
    }

    @Test
    fun onItemClickTest() {
        val presenter = BookmarkListPresenter()
        val bookmarks = ArrayList<Bookmark>()
        val bookmark = Bookmark(BookmarkResponse("user", emptyList(), "", false, ""), emptyList())
        presenter.onCreate(viewMock, bookmarks)
        presenter.onMoreIconClick(bookmark, "1", BookmarkPopupWindowBuilder.INDEX_SEARCH)
        verify(viewMock, times(1)).startUserSearchActivity("user")
        // TODO add tests
    }
}
