package me.kirimin.mitsumine.test;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.kirimin.mitsumine.data.network.api.FeedApi;
import me.kirimin.mitsumine.presenter.TopPresenter;
import me.kirimin.mitsumine.view.TopView;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class TopPresenterTest {

    TopView viewMock;

    @Before
    public void setup() {
        viewMock = mock(TopView.class);
    }

    @Test
    public void takeViewTest() {
        TopPresenter presenter = new TopPresenter();
        presenter.takeView(viewMock);
        verify(viewMock, times(1)).refreshShowCategoryAndType(FeedApi.TYPE.HOT);
    }

    @Test
    public void toolbarClickTest() {
        when(viewMock.isOpenNavigation()).thenReturn(false);

        TopPresenter presenter = new TopPresenter();
        presenter.takeView(viewMock);
        verify(viewMock, never()).openNavigation();
        verify(viewMock, never()).closeNavigation();

        presenter.onToolbarClick();
        verify(viewMock, times(1)).openNavigation();
        verify(viewMock, never()).closeNavigation();

        when(viewMock.isOpenNavigation()).thenReturn(true);
        presenter.onToolbarClick();
        verify(viewMock, times(1)).openNavigation();
        verify(viewMock, times(1)).closeNavigation();
    }
}
