package me.kirimin.mitsumine.entryinfo

import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert
import me.kirimin.mitsumine.BuildConfig

import org.junit.Before
import org.junit.Test

import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import rx.Observable
import java.net.URLEncoder

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class EntryInfoPresenterTest {

    @Rule
    @JvmField
    var mockito = MockitoJUnit.rule()

    @Mock
    lateinit var viewMock: EntryInfoView
    @Mock
    lateinit var useCaseMock: EntryInfoUseCase
    @Spy
    @InjectMocks
    lateinit var presenter: EntryInfoPresenter
    lateinit var entryInfo: EntryInfo

    @Before
    fun setup() {
        val bookmarks = listOf(
                BookmarkResponse(user = "test1", tags = listOf("TagA"), timestamp = "", private = false, comment = "comment"),
                BookmarkResponse(user = "test2", tags = emptyList(), timestamp = "", private = false, comment = ""),
                BookmarkResponse(user = "test3", tags = listOf("TagB", "TagC"), timestamp = "", private = false, comment = "comment"),
                BookmarkResponse(user = "test4", tags = listOf("TagB"), timestamp = "", private = false, comment = "")
        )
        entryInfo = EntryInfo(EntryInfoResponse("testA", 4, "http://sample", "http://thum", bookmarks))
        whenever(useCaseMock.requestEntryInfo(any())).thenReturn(Observable.just(entryInfo))
    }

    @Test
    fun onCreateTest() {
        whenever(useCaseMock.isLogin()).thenReturn(false)
        presenter.onCreate(viewMock, useCaseMock, "http://sample")
        verify(viewMock, times(1)).initActionBar()
        verify(useCaseMock, times(1)).requestEntryInfo(URLEncoder.encode("http://sample", "utf-8"))

        // 取得したものが設定される
        verify(viewMock, times(1)).setEntryInfo(entryInfo)
        // 非ログイン時は対象ページのブクマ登録Fragmentは設定されない
        verify(viewMock, never()).setRegisterBookmarkFragment("http://sample")
        // コメントありは2件
        verify(viewMock, times(1)).setCommentCount("2")
        // タグは多い順にカンマ区切り
        Assert.assertEquals(entryInfo.tagListString, "TagB, TagA, TagC")
        verify(viewMock, times(1)).setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
    }

    @Test
    fun onNextTestWithLogin() {
        whenever(useCaseMock.isLogin()).thenReturn(true)
        presenter.onCreate(viewMock, useCaseMock, "http://sample")
        verify(viewMock, times(1)).setEntryInfo(entryInfo)
        verify(viewMock, times(1)).setRegisterBookmarkFragment("http://sample")
    }
}
