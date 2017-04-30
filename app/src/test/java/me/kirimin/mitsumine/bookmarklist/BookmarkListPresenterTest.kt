package me.kirimin.mitsumine.bookmarklist

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import me.kirimin.mitsumine.BuildConfig
import org.junit.Test

import java.util.ArrayList

import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import org.junit.Before
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

    @Before
    fun setUp() {
        presenter.view = viewMock
    }

    @Test
    fun onCreateTest() {
        val bookmarks = emptyList<Bookmark>()
        presenter.onCreate(bookmarks)
        verify(viewMock, times(1)).initViews(bookmarks)
    }

    @Test
    fun onMoreIconClickAndUserSearchSelectedTest() {
        val bookmarks = emptyList<Bookmark>()
        val bookmark = Bookmark(BookmarkResponse("user", emptyList(), "", false, ""), emptyList())
        presenter.onCreate(bookmarks)
        presenter.onMoreIconClick(bookmark, "1", BookmarkPopupWindowBuilder.INDEX_SEARCH)
        verify(viewMock, times(1)).startUserSearchActivity("user")
    }

    @Test
    fun onMoreIconClickAndShareSelectedTest() {
        val bookmarks = emptyList<Bookmark>()
        val bookmark = Bookmark(BookmarkResponse("user", emptyList(), "", false, "comment"), emptyList())
        presenter.onCreate(bookmarks)
        presenter.onMoreIconClick(bookmark, "1", BookmarkPopupWindowBuilder.INDEX_SHARE)
        verify(viewMock, times(1)).shareCommentLink("\"comment\" id:user http://b.hatena.ne.jp/entry/1/comment/user")
    }

    @Test
    fun onMoreIconClickAndBrowserSelectedTest() {
        val bookmarks = emptyList<Bookmark>()
        val bookmark = Bookmark(BookmarkResponse("user", emptyList(), "", false, "comment"), emptyList())
        presenter.onCreate(bookmarks)
        presenter.onMoreIconClick(bookmark, "1", BookmarkPopupWindowBuilder.INDEX_BROWSER)
        verify(viewMock, times(1)).showBrowser("http://b.hatena.ne.jp/entry/1/comment/user")
    }

}
