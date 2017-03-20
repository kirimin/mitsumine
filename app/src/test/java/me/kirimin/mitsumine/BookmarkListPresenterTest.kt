package me.kirimin.mitsumine

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test

import java.util.ArrayList

import me.kirimin.mitsumine.bookmarklist.BookmarkListPresenter
import me.kirimin.mitsumine.bookmarklist.BookmarkListView
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine.bookmarklist.BookmarkPopupWindowBuilder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BookmarkListPresenterTest {

    lateinit var viewMock: BookmarkListView

    @Before
    fun setup() {
        viewMock = mock()
    }

    @Test
    fun onCreateTest() {
        val presenter = BookmarkListPresenter()
        val bookmarks = ArrayList<Bookmark>()
        presenter.onCreate(viewMock, bookmarks)
        verify(viewMock, times(1)).initViews(bookmarks)
    }

    @Test
    fun onItemClickTest() {
        val presenter = BookmarkListPresenter()
        val bookmarks = ArrayList<Bookmark>()
        val bookmark = Bookmark("user", emptyList(), "", "", "", emptyList())
        presenter.onCreate(viewMock, bookmarks)
        presenter.onMoreIconClick(bookmark, "1", BookmarkPopupWindowBuilder.INDEX_SEARCH)
        verify(viewMock, times(1)).startUserSearchActivity("user")
        // TODO add tests
    }
}
