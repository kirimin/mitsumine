package me.kirimin.mitsumine

import com.nhaarman.mockito_kotlin.anyList
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test

import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.feed.FeedPresenter
import me.kirimin.mitsumine.feed.FeedView
import rx.Observable

import org.mockito.Mockito.*

class FeedPresenterTest {

    lateinit var viewMock: FeedView
    lateinit var repositoryMock: AbstractFeedRepository
    val presenter = FeedPresenter()
    val resultMock = listOf(Feed(title = "mock1"), Feed(title = "mock2"))

    @Before
    fun setup() {
        
        viewMock = mock()
        repositoryMock = mock()
        whenever(repositoryMock.requestFeed()).thenReturn(Observable.from(resultMock))
    }

    @Test
    @JvmName(name = "onCreate時にフィードを読み込みセットする")
    fun onCreateTest() {
        presenter.onCreate(viewMock, repositoryMock)
        verify(viewMock, times(1)).initViews()
        verify(viewMock, times(1)).showRefreshing()
        verify(repositoryMock, times(1)).requestFeed()

        verify(viewMock, times(1)).setFeed(resultMock)
        verify(viewMock, times(1)).dismissRefreshing()
    }

    @Test
    @JvmName(name = "PullToRefresh時にフィードを更新する")
    fun onRefreshTest() {
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onRefresh()
        verify(viewMock, times(1)).clearAllItem()
        verify(viewMock, times(2)).showRefreshing()
        verify(repositoryMock, times(2)).requestFeed()

        verify(viewMock, times(2)).setFeed(resultMock)
        verify(viewMock, times(2)).dismissRefreshing()
    }

    @Test
    @JvmName(name = "フィードデータ取得失敗時にインジケータを停止する")
    fun onErrorTest() {
        whenever(repositoryMock.requestFeed()).thenReturn(Observable.error(Exception()))
        presenter.onCreate(viewMock, repositoryMock)
        verify(viewMock, never()).setFeed(anyList())
        verify(viewMock, times(1)).dismissRefreshing()
    }

    @Test
    @JvmName(name = "アイテムクリック時にURLをブラウザで開く")
    fun onItemClick() {
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onItemClick(Feed(linkUrl = "http://test"))
        verify(viewMock, times(1)).sendUrlIntent("http://test")
    }

    @Test
    @JvmName(name = "アイテム長押し時にコメント一覧を開く")
    fun onItemLongClick() {
        val feed = Feed()
        feed.linkUrl = "http://test"
        feed.entryLinkUrl = "http://entry"

        // ブラウザで開く
        whenever(repositoryMock.isUseBrowserSettingEnable).thenReturn(true)
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onItemLongClick(feed)
        verify(viewMock, times(1)).sendUrlIntent("http://entry")
        verify(viewMock, never()).startEntryInfoView("http://test")

        // ネイティブで開く
        whenever(repositoryMock.isUseBrowserSettingEnable).thenReturn(false)
        presenter.onItemLongClick(feed)
        verify(viewMock, times(1)).sendUrlIntent("http://entry")
        verify(viewMock, times(1)).startEntryInfoView("http://test")
    }

    @Test
    @JvmName(name = "シェアボタン押下と長押しでタイトルを付けるかを切り替える")
    fun onFeedShareClick() {
        val feed = Feed()
        feed.title = "title"
        feed.linkUrl = "http://test"
        feed.entryLinkUrl = "http://entry"

        // 押下
        // タイトル入り設定
        whenever(repositoryMock.isShareWithTitleSettingEnable).thenReturn(true)
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onFeedShareClick(feed)
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, never()).sendShareUrlIntent("title", "http://test")

        // タイトル無し設定
        whenever(repositoryMock.isShareWithTitleSettingEnable).thenReturn(false)
        presenter.onFeedShareClick(feed)
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, times(1)).sendShareUrlIntent("title", "http://test")

        // 長押し
        // タイトル入り設定
        `when`(repositoryMock.isShareWithTitleSettingEnable).thenReturn(true)
        val presenter = FeedPresenter()
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onFeedShareLongClick(feed)
        verify(viewMock, times(1)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, times(2)).sendShareUrlIntent("title", "http://test")

        // タイトル無し設定
        `when`(repositoryMock.isShareWithTitleSettingEnable).thenReturn(false)
        presenter.onFeedShareLongClick(feed)
        verify(viewMock, times(2)).sendShareUrlWithTitleIntent("title", "http://test")
        verify(viewMock, times(2)).sendShareUrlIntent("title", "http://test")
    }
}
