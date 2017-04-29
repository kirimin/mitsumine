package me.kirimin.mitsumine.top

import android.view.View
import com.nhaarman.mockito_kotlin.*
import me.kirimin.mitsumine.BuildConfig

import org.junit.Assert
import org.junit.Test

import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.model.Account
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class TopPresenterTest {

    @Rule
    @JvmField
    var mockito = MockitoJUnit.rule()

    @Mock
    lateinit var viewMock: TopView
    @Mock
    lateinit var useCaseMock: TopUseCase
    @Spy
    @InjectMocks
    lateinit var presenter: TopPresenter

    @Test
    fun onCreateTest() {
        presenter.onCreate(viewMock, useCaseMock)
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
    fun defaultShowCategoryAndTypeTest() {
        presenter.onCreate(viewMock, useCaseMock)
        verify(viewMock, times(1)).refreshShowCategoryAndType(Category.MAIN, Type.HOT)
    }

    @Test
    fun ShowSelectedCategoryAndTypeTestOnCreateTest() {
        presenter.onCreate(viewMock, useCaseMock, Category.IT, Type.NEW)
        verify(viewMock, times(1)).refreshShowCategoryAndType(Category.IT, Type.NEW)
    }

    @Test
    fun userInfoDisableTest() {
        whenever(useCaseMock.account).then { null }
        presenter.onCreate(viewMock, useCaseMock)
        presenter.onStart()
        verify(viewMock, times(1)).removeNavigationAdditions()
        verify(viewMock, times(1)).disableUserInfo()
        verify(viewMock, never()).enableUserInfo(any(), any())
    }

    @Test
    fun userInfoEnableTest() {
        val account = mock<Account>()
        whenever(account.displayName).thenReturn("kirimin")
        whenever(account.imageUrl).thenReturn("image.png")
        whenever(useCaseMock.account).then { account }
        presenter.onCreate(viewMock, useCaseMock)
        presenter.onStart()
        verify(viewMock, times(1)).removeNavigationAdditions()
        verify(viewMock, never()).disableUserInfo()
        verify(viewMock, times(1)).enableUserInfo(userName = "kirimin", iconUrl = "image.png")
    }

    @Test
    fun additionUserTest() {
        val users = listOf("testA", "testB", "testC")
        whenever(useCaseMock.additionUsers).thenReturn(users)

        // 表示
        presenter.onCreate(viewMock, useCaseMock)
        presenter.onStart()
        verify(useCaseMock, times(1)).additionUsers
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
        verify(useCaseMock, never()).deleteAdditionUser("testB")

        // ダイアログOK
        presenter.onDeleteUserIdDialogClick("testB", testBView)
        verify(useCaseMock, times(1)).deleteAdditionUser("testB")
        verify(useCaseMock, never()).deleteAdditionUser("testA")
        verify(useCaseMock, never()).deleteAdditionUser("testC")
        verify(testBView, times(1)).visibility = View.GONE
    }

    @Test
    fun additionKeywordTest() {
        val keywords = listOf("testA", "testB", "testC")
        whenever(useCaseMock.additionKeywords).thenReturn(keywords)

        presenter.onCreate(viewMock, useCaseMock)
        presenter.onStart()
        verify(useCaseMock, times(1)).additionKeywords
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
        verify(useCaseMock, never()).deleteAdditionKeyword("testB")

        presenter.onDeleteKeywordDialogClick("testB", testBView)
        verify(useCaseMock, times(1)).deleteAdditionKeyword("testB")
        verify(useCaseMock, never()).deleteAdditionKeyword("testA")
        verify(useCaseMock, never()).deleteAdditionKeyword("testC")
        verify(testBView, times(1)).visibility = View.GONE
    }

    @Test
    fun toolbarClickTest() {
        whenever(viewMock.isOpenNavigation()).thenReturn(false)

        val presenter = TopPresenter()
        presenter.onCreate(viewMock, useCaseMock, Category.MAIN, Type.HOT)
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
    fun typeSelectTest() {
        val presenter = TopPresenter()
        presenter.onCreate(viewMock, useCaseMock, Category.MAIN, Type.HOT)
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
    fun backKeyPressTest() {
        whenever(viewMock.isOpenNavigation()).thenReturn(false)

        val presenter = TopPresenter()
        presenter.onCreate(viewMock, useCaseMock, Category.MAIN, Type.HOT)
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
    fun deleteOldDataTest() {
        presenter.onCreate(viewMock, useCaseMock)
        verify(useCaseMock, times(1)).deleteOldFeedData(3)
    }
}
