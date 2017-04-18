package me.kirimin.mitsumine.entryinfo

import android.content.Context
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert

import org.junit.Before
import org.junit.Test

import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine.entryinfo.EntryInfoPresenter
import me.kirimin.mitsumine.entryinfo.EntryInfoRepository
import me.kirimin.mitsumine.entryinfo.EntryInfoView
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness

import rx.Observable
import java.net.URLEncoder

@RunWith(JUnit4::class)
class EntryInfoPresenterTest {

    @Rule
    @JvmField
    var rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    lateinit var viewMock: EntryInfoView
    @Mock
    lateinit var repositoryMock: EntryInfoRepository
    @Mock
    lateinit var contextMock: Context
    @Mock
    lateinit var resultMock: EntryInfo
    @Spy
    lateinit var presenter: EntryInfoPresenter

    @Before
    fun setup() {
        val bookmarks = listOf(
                Bookmark("test1", listOf("TagA"), "", "comment", "", emptyList()),
                Bookmark("test2", emptyList(), "", "", "", emptyList()),
                Bookmark("test3", listOf("TagB", "TagC"), "", "comment", "", emptyList()),
                Bookmark("test4", listOf("TagB"), "", "", "", emptyList())
        )
        resultMock = EntryInfo("testA", 4, "http://sample", "http://thum", bookmarks)
        whenever(repositoryMock.requestEntryInfoApi(any(), any())).thenReturn(Observable.just(resultMock))
    }

    @Test
    fun onCreateTest() {
        whenever(repositoryMock.isLogin()).thenReturn(false)
        presenter.onCreate(viewMock, repositoryMock, "http://sample", contextMock)
        verify(viewMock, times(1)).initActionBar()
        verify(repositoryMock, times(1)).requestEntryInfoApi(contextMock, URLEncoder.encode("http://sample", "utf-8"))

        // 取得したものが設定される
        verify(viewMock, times(1)).setEntryInfo(resultMock)
        // 非ログイン時は対象ページのブクマ登録Fragmentは設定されない
        verify(viewMock, never()).setRegisterBookmarkFragment("http://sample")
        // コメントありは2件
        verify(viewMock, times(1)).setCommentCount("2")
        // タグは多い順にカンマ区切り
        Assert.assertEquals(resultMock.tagListString(), "TagB, TagA, TagC")
        verify(viewMock, times(1)).setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
    }

    @Test
    fun onNextTestWithLogin() {
        whenever(repositoryMock.isLogin()).thenReturn(true)
        presenter.onCreate(viewMock, repositoryMock, "http://sample", contextMock)
        verify(viewMock, times(1)).setEntryInfo(resultMock)
        verify(viewMock, times(1)).setRegisterBookmarkFragment("http://sample")
    }
}
