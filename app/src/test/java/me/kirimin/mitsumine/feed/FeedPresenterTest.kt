package me.kirimin.mitsumine.feed

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import me.kirimin.mitsumine.BuildConfig
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import org.junit.Before
import org.junit.Test

import me.kirimin.mitsumine._common.domain.model.Feed
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import rx.Observable

import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class FeedPresenterTest {

    @Rule
    @JvmField
    var mockito = MockitoJUnit.rule()

    @Mock
    lateinit var viewMock: FeedView
    @Mock
    lateinit var useCaseMock: FeedUseCase
    @Spy
    @InjectMocks
    lateinit var presenter : FeedPresenter

    val resultMock = listOf<Feed>(mock(), mock())
    var feedMethod = FeedPresenter.FeedMethod.MainFeed(category = Category.MAIN, type = Type.HOT)

    @Before
    fun setup() {
        whenever(useCaseMock.requestMainFeed(any(), any())).thenReturn(Observable.from(resultMock))
    }

    @Test
    fun onCreateTest() {
        presenter.onCreate(feedMethod)
        verify(viewMock, times(1)).initViews(feedMethod.isUseRead, feedMethod.isUseReadLater)
        verify(viewMock, times(1)).showRefreshing()
        verify(useCaseMock, times(1)).requestMainFeed(Category.MAIN, Type.HOT)

        verify(viewMock, times(1)).setFeed(resultMock)
        verify(viewMock, times(1)).dismissRefreshing()
    }

    @Test
    fun onRefreshTest() {
        presenter.onCreate(feedMethod)
        presenter.onRefresh()
        verify(viewMock, times(1)).clearAllItem()
        verify(viewMock, times(2)).showRefreshing()
        verify(useCaseMock, times(2)).requestMainFeed(Category.MAIN, Type.HOT)

        verify(viewMock, times(2)).setFeed(resultMock)
        verify(viewMock, times(2)).dismissRefreshing()
    }

    @Test
    fun onErrorTest() {
        whenever(useCaseMock.requestMainFeed(any(), any())).thenReturn(Observable.error(Exception()))
        presenter.onCreate(feedMethod)
        verify(viewMock, never()).setFeed(anyList())
        verify(viewMock, times(1)).dismissRefreshing()
    }

    @Test
    fun onItemClick() {
        presenter.onCreate(feedMethod)
        val feed = mock<Feed>()
        whenever(feed.linkUrl).thenReturn("http://test")
        presenter.onItemClick(feed)
        verify(viewMock, times(1)).sendUrlIntent("http://test")
    }

    @Test
    fun onItemLongClickTest() {
        val feed = mock<Feed>()
        whenever(feed.linkUrl).thenReturn("http://test")
        whenever(feed.entryLinkUrl).thenReturn("http://entry")

        // ブラウザで開く
        whenever(useCaseMock.isUseBrowserSettingEnable).thenReturn(true)
        presenter.onCreate(feedMethod)
        presenter.onItemLongClick(feed)
        verify(viewMock, times(1)).sendUrlIntent("http://entry")
        verify(viewMock, never()).startEntryInfoView("http://test")

        // ネイティブで開く
        whenever(useCaseMock.isUseBrowserSettingEnable).thenReturn(false)
        presenter.onItemLongClick(feed)
        verify(viewMock, times(1)).sendUrlIntent("http://entry")
        verify(viewMock, times(1)).startEntryInfoView("http://test")
    }

    @Test
    fun onFeedShareClickTest() {
        val feed = mock<Feed>()
        whenever(feed.title).thenReturn("title")
        whenever(feed.linkUrl).thenReturn("http://test")

        // 押下
        // タイトル入り設定
        whenever(useCaseMock.isShareWithTitleSettingEnable).thenReturn(true)
        presenter.onCreate(feedMethod)
        presenter.onFeedShareClick(feed)
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, never()).sendShareUrlIntent("title", "http://test")

        // タイトル無し設定
        whenever(useCaseMock.isShareWithTitleSettingEnable).thenReturn(false)
        presenter.onFeedShareClick(feed)
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, times(1)).sendShareUrlIntent("title", "http://test")

        // 長押し
        // タイトル入り設定
        `when`(useCaseMock.isShareWithTitleSettingEnable).thenReturn(true)
        presenter.onCreate(feedMethod)
        presenter.onFeedShareLongClick(feed)
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, times(2)).sendShareUrlIntent("title", "http://test")

        // タイトル無し設定
        `when`(useCaseMock.isShareWithTitleSettingEnable).thenReturn(false)
        presenter.onFeedShareLongClick(feed)
        verify(viewMock, times(2)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, times(2)).sendShareUrlIntent("title", "http://test")
    }
}
