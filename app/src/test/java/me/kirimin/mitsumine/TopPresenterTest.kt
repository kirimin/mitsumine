package me.kirimin.mitsumine

import android.view.View
import com.nhaarman.mockito_kotlin.*

import org.junit.Assert
import org.junit.Before
import org.junit.Test

import java.util.ArrayList

import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.model.Account
import me.kirimin.mitsumine.top.TopRepository
import me.kirimin.mitsumine.top.TopPresenter
import me.kirimin.mitsumine.top.TopView

class TopPresenterTest {

    lateinit var viewMock: TopView
    lateinit var repositoryMock: TopRepository
    val presenter = TopPresenter()

    @Before
    fun setup() {
        viewMock = mock()
        repositoryMock = mock()
    }

    @Test
    @JvmName(name = "onCreate時にNavigationBarに全カテゴリが追加される")
    fun onCreateTest() {
        presenter.onCreate(viewMock, repositoryMock)
        verify(viewMock, times(1)).initViews()
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.MAIN)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.SOCIAL)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.ECONOMICS)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.LIFE)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.KNOWLEDGE)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.IT)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.FUN)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.ENTERTAINMENT)
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.GAME)
    }

    @Test
    @JvmName(name = "onCreate時にデフォルトで総合・人気が表示される")
    fun onCreateTest2() {
        presenter.onCreate(viewMock, repositoryMock)
        verify(viewMock, times(1)).refreshShowCategoryAndType(Category.MAIN, Type.HOT)
    }

    @Test
    @JvmName(name = "onCreate時に受け取ったカテゴリ・タイプが表示される")
    fun onCreateTest3() {
        presenter.onCreate(viewMock, repositoryMock, Category.IT, Type.NEW)
        verify(viewMock, times(1)).refreshShowCategoryAndType(Category.IT, Type.NEW)
    }

    @Test
    @JvmName(name = "画面表示時にログインしてなければユーザー情報が非表示になりログインボタンが表示される")
    fun userInfoDisableTest() {
        whenever(repositoryMock.account).then { null }
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onStart()
        verify(viewMock, times(1)).removeNavigationAdditions()
        verify(viewMock, times(1)).disableUserInfo()
        verify(viewMock, never()).enableUserInfo(any(), any())
    }

    @Test
    @JvmName(name = "画面表示時にログインしていればユーザー情報が表示されログインボタンが非表示になる")
    fun userInfoEnableTest() {
        whenever(repositoryMock.account).then { Account(displayName = "kirimin", imageUrl = "image.png") }
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onStart()
        verify(viewMock, times(1)).removeNavigationAdditions()
        verify(viewMock, never()).disableUserInfo()
        verify(viewMock, times(1)).enableUserInfo(userName = "kirimin", iconUrl = "image.png")
    }

    @Test
    @JvmName(name = "画面表示時に登録しているユーザーが表示される。クリックでユーザー検索に遷移。ロングクリックで削除ダイアログ表示")
    fun additionUserTest() {
        val users = listOf("testA", "testB", "testC")
        whenever(repositoryMock.additionUsers).thenReturn(users)

        // 表示
        presenter.onCreate(viewMock, repositoryMock)
        presenter.onStart()
        verify(repositoryMock, times(1)).additionUsers
        verify(viewMock, times(1)).addAdditionUser("testA")
        verify(viewMock, times(1)).addAdditionUser("testB")
        verify(viewMock, times(1)).addAdditionUser("testC")

        val testBView: View = mock()
        // クリック
        presenter.onAdditionUserClick("testB")
        verify(viewMock, times(1)).startUserSearchActivity("testB")
        verify(viewMock, times(1)).closeNavigation()

        // ロングクリック
        presenter.onAdditionUserLongClick("testB", testBView)
        verify(viewMock, times(1)).showDeleteUserDialog("testB", testBView)
        verify(viewMock, never()).showDeleteUserDialog(eq("testA"), any())
        verify(viewMock, never()).showDeleteUserDialog(eq("testC"), any())
        verify(repositoryMock, never()).deleteAdditionUser("testB")

        // ダイアログOK
        presenter.onDeleteUserIdDialogClick("testB", testBView)
        verify(repositoryMock, times(1)).deleteAdditionUser("testB")
        verify(repositoryMock, never()).deleteAdditionUser("testA")
        verify(repositoryMock, never()).deleteAdditionUser("testC")
        verify(testBView, times(1)).visibility = View.GONE
    }

    @Test
    @JvmName(name = "画面表示時に登録しているキーワードが表示される。クリックでキーワード検索に遷移。ロングクリックで削除ダイアログ表示")
    fun additionKeywordTest() {
        val keywords = listOf("testA", "testB", "testC")
        whenever(repositoryMock.additionKeywords).thenReturn(keywords)

        presenter.onCreate(viewMock, repositoryMock)
        presenter.onStart()
        verify(repositoryMock, times(1)).additionKeywords
        verify(viewMock, times(1)).addAdditionKeyword("testA")
        verify(viewMock, times(1)).addAdditionKeyword("testB")
        verify(viewMock, times(1)).addAdditionKeyword("testC")

        val testBView: View = mock()
        presenter.onAdditionKeywordClick("testB")
        verify(viewMock, times(1)).startKeywordSearchActivity("testB")
        verify(viewMock, times(1)).closeNavigation()

        presenter.onAdditionKeywordLongClick("testB", testBView)
        verify(viewMock, times(1)).showDeleteKeywordDialog("testB", testBView)
        verify(viewMock, never()).showDeleteKeywordDialog(eq("testA"), any())
        verify(viewMock, never()).showDeleteKeywordDialog(eq("testC"), any())
        verify(repositoryMock, never()).deleteAdditionKeyword("testB")

        presenter.onDeleteKeywordDialogClick("testB", testBView)
        verify(repositoryMock, times(1)).deleteAdditionKeyword("testB")
        verify(repositoryMock, never()).deleteAdditionKeyword("testA")
        verify(repositoryMock, never()).deleteAdditionKeyword("testC")
        verify(testBView, times(1)).visibility = View.GONE
    }

    @Test
    @JvmName(name = "ツールバークリックでNavigationBarが開閉する")
    fun toolbarClickTest() {
        whenever(viewMock.isOpenNavigation()).thenReturn(false)

        val presenter = TopPresenter()
        presenter.onCreate(viewMock, repositoryMock, Category.MAIN, Type.HOT)
        presenter.onStart()
        verify(viewMock, never()).openNavigation()
        verify(viewMock, never()).closeNavigation()

        presenter.onToolbarClick()
        verify(viewMock, times(1)).openNavigation()
        verify(viewMock, never()).closeNavigation()

        whenever(viewMock.isOpenNavigation()).thenReturn(true)
        presenter.onToolbarClick()
        verify(viewMock, times(1)).openNavigation()
        verify(viewMock, times(1)).closeNavigation()
    }

    @Test
    @JvmName(name = "人気・新着の切替えが出来る")
    fun typeSelectTest() {
        val presenter = TopPresenter()
        presenter.onCreate(viewMock, repositoryMock, Category.MAIN, Type.HOT)
        presenter.onStart()
        Assert.assertEquals(presenter.selectedType, Type.HOT)
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(), eq(Type.HOT))
        verify(viewMock, never()).refreshShowCategoryAndType(any(), eq(Type.NEW))

        presenter.onNavigationClick(1)
        Assert.assertEquals(presenter.selectedType, Type.NEW)
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(), eq(Type.HOT))
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(), eq(Type.NEW))

        presenter.onNavigationClick(0)
        Assert.assertEquals(presenter.selectedType, Type.HOT)
        verify(viewMock, times(2)).refreshShowCategoryAndType(any(), eq(Type.HOT))
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(), eq(Type.NEW))
    }

    @Test
    @JvmName(name = "バックキー押下時にNavigationBarが開いていたら閉じ、閉じていたら画面を終了する")
    fun backKeyPressTest() {
        whenever(viewMock.isOpenNavigation()).thenReturn(false)

        val presenter = TopPresenter()
        presenter.onCreate(viewMock, repositoryMock, Category.MAIN, Type.HOT)
        presenter.onStart()
        presenter.onBackKeyClick()
        verify(viewMock, times(1)).backPress()
        verify(viewMock, never()).closeNavigation()

        whenever(viewMock.isOpenNavigation()).thenReturn(true)
        presenter.onBackKeyClick()
        verify(viewMock, times(1)).backPress()
        verify(viewMock, times(1)).closeNavigation()
    }

    @Test
    @JvmName(name = "onCreate時に古い既読データを削除する")
    fun deleteOldDataTest() {
        presenter.onCreate(viewMock, repositoryMock)
        verify(repositoryMock, times(1)).deleteOldFeedData(3)
    }
}
