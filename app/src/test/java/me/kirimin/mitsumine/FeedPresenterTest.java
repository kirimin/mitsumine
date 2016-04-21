package me.kirimin.mitsumine;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.common.domain.model.Feed;
import me.kirimin.mitsumine.feed.FeedPresenter;
import me.kirimin.mitsumine.feed.FeedUseCase;
import me.kirimin.mitsumine.feed.FeedView;
import rx.Observer;
import static org.mockito.Mockito.*;

public class FeedPresenterTest {

    FeedView viewMock;
    FeedUseCase useCaseMock;

    @Before
    public void setup() {
        viewMock = mock(FeedView.class);
        useCaseMock = mock(FeedUseCase.class);
    }

    @Test
    public void onCreateTest() {
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);

        // それぞれのメソッドが１回だけ呼ばれたかをチェック
        verify(viewMock, times(1)).initViews();
        verify(viewMock, times(1)).showRefreshing();
        verify(useCaseMock, times(1)).requestFeed(any(Observer.class));
    }

    @Test
    public void onRefreshTest() {
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onRefresh();
        verify(viewMock, times(1)).clearAllItem();
        verify(viewMock, times(2)).showRefreshing();
        verify(useCaseMock, times(2)).requestFeed(any(Observer.class));
    }

    @Test
    public void onNextTest() {
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        List<Feed> list = new ArrayList<>();
        presenter.onNext(list);
        verify(viewMock, times(1)).setFeed(list);
    }

    @Test
    public void onErrorTest() {
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onError(new Throwable());
        verify(viewMock, never()).setFeed(any(List.class));
        verify(viewMock, times(1)).dismissRefreshing();
    }

    @Test
    public void onCompleteTest() {
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onCompleted();
        verify(viewMock, never()).setFeed(any(List.class));
        verify(viewMock, times(1)).dismissRefreshing();
    }

    @Test
    public void onItemClick() {
        Feed feed = new Feed();
        feed.setLinkUrl("http://test");

        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onItemClick(feed);
        verify(viewMock, times(1)).sendUrlIntent("http://test");
    }

    @Test
    public void onItemLongClick() {
        Feed feed = new Feed();
        feed.setLinkUrl("http://test");
        feed.setEntryLinkUrl("http://entry");

        when(useCaseMock.isUseBrowserSettingEnable()).thenReturn(true);
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onItemLongClick(feed);
        verify(viewMock, times(1)).sendUrlIntent("http://entry");
        verify(viewMock, never()).startEntryInfoView("http://test");

        when(useCaseMock.isUseBrowserSettingEnable()).thenReturn(false);
        presenter.onItemLongClick(feed);
        verify(viewMock, times(1)).sendUrlIntent("http://entry");
        verify(viewMock, times(1)).startEntryInfoView(("http://test"));
    }

    @Test
    public void onFeedShareClick() {
        Feed feed = new Feed();
        feed.setTitle("title");
        feed.setLinkUrl("http://test");
        feed.setEntryLinkUrl("http://entry");

        when(useCaseMock.isShareWithTitleSettingEnable()).thenReturn(true);
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onFeedShareClick(feed);
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test");
        verify(viewMock, never()).sendShareUrlIntent("title", "http://test");

        when(useCaseMock.isShareWithTitleSettingEnable()).thenReturn(false);
        presenter.onFeedShareClick(feed);
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test");
        verify(viewMock, times(1)).sendShareUrlIntent("title", "http://test");
    }

    @Test
    public void onFeedShareLongClick() {
        Feed feed = new Feed();
        feed.setTitle("title");
        feed.setLinkUrl("http://test");
        feed.setEntryLinkUrl("http://entry");

        when(useCaseMock.isShareWithTitleSettingEnable()).thenReturn(true);
        FeedPresenter presenter = new FeedPresenter();
        presenter.onCreate(viewMock, useCaseMock);
        presenter.onFeedShareLongClick(feed);
        verify(viewMock, never()).sendShareUrlWithTitleIntent("title", "http://test");
        verify(viewMock, times(1)).sendShareUrlIntent("title", "http://test");

        when(useCaseMock.isShareWithTitleSettingEnable()).thenReturn(false);
        presenter.onFeedShareLongClick(feed);
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test");
        verify(viewMock, times(1)).sendShareUrlIntent("title", "http://test");
    }

    @Test
    public void onFeedLeftSlideTest() {
//        Feed feed = new Feed();
//        FeedPresenter presenter = new FeedPresenter();
//        presenter.onCreate(viewMock, useCaseMock);
//        presenter.onFeedLeftSlide(feed);
//        verify(viewMock, times(1)).removeItem(feed);
//        verify(useCaseMock, times(1)).saveFeed(feed, Feed.TYPE_READ);
//        verify(useCaseMock, never()).saveFeed(feed, Feed.TYPE_READ_LATER);
    }

    @Test
    public void onFeedRightSlideTest() {
//        Feed feed = new Feed();
//        FeedPresenter presenter = new FeedPresenter();
//        presenter.onCreate(viewMock, useCaseMock);
//        presenter.onFeedRightSlide(feed);
//        verify(viewMock, times(1)).removeItem(feed);
//        verify(useCaseMock, never()).saveFeed(feed, Feed.TYPE_READ);
//        verify(useCaseMock, times(1)).saveFeed(feed, Feed.TYPE_READ_LATER);
    }
}
