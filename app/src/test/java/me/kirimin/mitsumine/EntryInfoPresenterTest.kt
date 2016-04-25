package me.kirimin.mitsumine

import android.content.Context
import com.nhaarman.mockito_kotlin.*

import org.junit.Before
import org.junit.Test

import java.util.ArrayList

import me.kirimin.mitsumine.common.domain.model.Bookmark
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import me.kirimin.mitsumine.entryinfo.EntryInfoPresenter
import me.kirimin.mitsumine.entryinfo.EntryInfoRepository
import me.kirimin.mitsumine.entryinfo.EntryInfoView

import rx.Observable

class EntryInfoPresenterTest {

    lateinit var viewMock: EntryInfoView
    lateinit var repositoryMock: EntryInfoRepository
    lateinit var contextMock: Context

    val presenter = EntryInfoPresenter()

    @Before
    fun setup() {
        viewMock = mock()
        repositoryMock = mock()
        contextMock = mock()
        whenever(repositoryMock.requestEntryInfoApi(any(), any())).thenReturn(Observable.never())
    }

    @Test
    @JvmName(name = "onCreate時にページ情報のリクエストが投げられる")
    fun onCreateTest() {
        presenter.onCreate(viewMock, repositoryMock, "http://sample", contextMock)
        verify(viewMock, times(1)).initActionBar()
        verify(repositoryMock, times(1)).requestEntryInfoApi(contextMock, "http://sample")
    }

    @Test
    @JvmName(name = "ページ情報取得成功時に情報が設定される")
    fun onNextTest() {
        presenter.onCreate(viewMock, repositoryMock, "http://sample", mock())
        val bookmarks = listOf(
                Bookmark("test1", emptyList(), "", "comment", ""),
                Bookmark("test2", emptyList(), "", "", ""),
                Bookmark("test3", emptyList(), "", "comment", "")
        )
        val entryInfo = EntryInfo("testA", 1, "http://sample", "http://thum", bookmarks, emptyList())

        whenever(repositoryMock.isLogin()).thenReturn(false)
        presenter.onNext(entryInfo)
        verify(viewMock, times(1)).setEntryInfo(entryInfo)
        // 非ログイン時は対象ページのブクマ登録Fragmentは設定されない
        verify(viewMock, never()).setRegisterBookmarkFragment("http://sample")
        // コメントありは2件
        verify(viewMock, times(1)).setCommentCount("2")
        verify(viewMock, times(1)).setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
    }

    @Test
    @JvmName(name = "ログイン時にはブクマ登録Fragmentが追加される")
    fun onNextTestWithLogin() {
        presenter.onCreate(viewMock, repositoryMock, "http://sample", mock())
        val bookmarks = listOf(Bookmark("test1", emptyList(), "", "comment", ""))
        val entryInfo = EntryInfo("testA", 1, "http://sample", "http://thum", bookmarks, emptyList())

        whenever(repositoryMock.isLogin()).thenReturn(true)
        presenter.onNext(entryInfo)
        verify(viewMock, times(1)).setEntryInfo(entryInfo)
        verify(viewMock, times(1)).setRegisterBookmarkFragment("http://sample")
    }
}
