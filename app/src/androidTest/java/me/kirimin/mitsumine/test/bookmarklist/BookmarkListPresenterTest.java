package me.kirimin.mitsumine.test.bookmarklist;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.domain.model.Bookmark;
import me.kirimin.mitsumine.bookmarklist.BookmarkListPresenter;
import me.kirimin.mitsumine.bookmarklist.BookmarkListView;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
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
