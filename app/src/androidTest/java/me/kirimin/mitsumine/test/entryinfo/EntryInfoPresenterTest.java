package me.kirimin.mitsumine.test.entryinfo;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.domain.usecase.EntryInfoUseCase;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;
import me.kirimin.mitsumine.presenter.EntryInfoPresenter;
import me.kirimin.mitsumine.view.EntryInfoView;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class EntryInfoPresenterTest {

    EntryInfoView viewMock;
    EntryInfoUseCase useCaseMock;
    Context contextMock;

    @Before
    public void setup() {
        viewMock = mock(EntryInfoView.class);
        useCaseMock = mock(EntryInfoUseCase.class);
        contextMock = mock(Context.class);
    }

    @Test
    public void onCreateTest() {
        EntryInfoPresenter presenter = new EntryInfoPresenter();
        presenter.onCreate(viewMock, useCaseMock, "http://sample", contextMock);
        verify(viewMock, times(1)).initActionBar();
        verify(useCaseMock, times(1)).requestEntryInfo(eq("http://sample"), eq(contextMock), any(EntryInfoPresenter.class));
    }

    @Test
    public void onNextTest() {
        EntryInfoPresenter presenter = new EntryInfoPresenter();
        presenter.onCreate(viewMock, useCaseMock, "http://sample", mock(Context.class));

        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(new Bookmark("test1", new ArrayList<String>(), "", "comment", ""));
        bookmarks.add(new Bookmark("test2", new ArrayList<String>(), "", "", ""));
        bookmarks.add(new Bookmark("test3", new ArrayList<String>(), "", "comment", ""));
        EntryInfo entryInfo = new EntryInfo("testA", 1, "http://sample", "http://thum", bookmarks, new ArrayList<String>());

        presenter.onNext(entryInfo);
        verify(viewMock, times(1)).setEntryInfo(entryInfo);
        verify(viewMock, times(1)).setBookmarkFragments(bookmarks, useCaseMock.getHasCommentBookmarks(bookmarks));
        verify(viewMock, never()).setRegisterBookmarkFragment(eq("http://sample"));
        verify(viewMock, times(1)).setCommentCount(eq("0"));
        verify(viewMock, times(1)).setViewPagerSettings(1, 2);
    }

    @Test
    public void onNextTestWithLogin() {
        EntryInfoPresenter presenter = new EntryInfoPresenter();
        presenter.onCreate(viewMock, useCaseMock, "http://sample", mock(Context.class));

        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(new Bookmark("test1", new ArrayList<String>(), "", "comment", ""));
        bookmarks.add(new Bookmark("test2", new ArrayList<String>(), "", "", ""));
        bookmarks.add(new Bookmark("test3", new ArrayList<String>(), "", "comment", ""));
        EntryInfo entryInfo = new EntryInfo("testA", 1, "http://sample", "http://thum", bookmarks, new ArrayList<String>());

        when(useCaseMock.isLogin()).thenReturn(true);
        presenter.onNext(entryInfo);
        verify(viewMock, times(1)).setEntryInfo(entryInfo);
        verify(viewMock, times(1)).setBookmarkFragments(bookmarks, useCaseMock.getHasCommentBookmarks(bookmarks));
        verify(viewMock, times(1)).setRegisterBookmarkFragment(eq("http://sample"));
        verify(viewMock, times(1)).setCommentCount(eq("0"));
        verify(viewMock, times(1)).setViewPagerSettings(1, 2);
    }
}
