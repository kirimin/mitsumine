package me.kirimin.mitsumine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.bookmarklist.BookmarkListPresenter;
import me.kirimin.mitsumine.bookmarklist.BookmarkListView;
import me.kirimin.mitsumine._common.domain.model.Bookmark;

import static org.mockito.Mockito.*;

public class BookmarkListPresenterTest {

    BookmarkListView viewMock;

    @Before
    public void setup() {
        viewMock = mock(BookmarkListView.class);
    }

    @Test
    public void onCreateTest() {
        BookmarkListPresenter presenter = new BookmarkListPresenter();
        List<Bookmark> bookmarks = new ArrayList<>();
        presenter.onCreate(viewMock, bookmarks);
        verify(viewMock, times(1)).initViews(bookmarks);
    }

    @Test
    public void onItemClickTest() {
        BookmarkListPresenter presenter = new BookmarkListPresenter();
        List<Bookmark> bookmarks = new ArrayList<>();
        Bookmark bookmark = new Bookmark("user", new ArrayList<String>(), "", "", "");
        presenter.onCreate(viewMock, bookmarks);
        presenter.onItemClick(bookmark);
        verify(viewMock, times(1)).startUserSearchActivity(eq("user"));
    }
}
