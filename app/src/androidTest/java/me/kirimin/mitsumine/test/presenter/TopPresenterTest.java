package me.kirimin.mitsumine.test.presenter;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.domain.usecase.TopUseCase;
import me.kirimin.mitsumine.model.enums.Category;
import me.kirimin.mitsumine.model.enums.Type;
import me.kirimin.mitsumine.presenter.TopPresenter;
import me.kirimin.mitsumine.view.TopView;
import me.kirimin.mitsumine.view.activity.search.KeywordSearchActivity;
import me.kirimin.mitsumine.view.activity.search.SearchActivity;
import me.kirimin.mitsumine.view.activity.search.UserSearchActivity;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class TopPresenterTest {

    TopView viewMock;
    TopUseCase useCaseMock;

    @Before
    public void setup() {
        viewMock = mock(TopView.class);
        useCaseMock = mock(TopUseCase.class);
    }

    @Test
    public void onCreateTest() {
        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        verify(viewMock, times(1)).initViews();
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.MAIN);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.SOCIAL);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.ECONOMICS);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.LIFE);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.KNOWLEDGE);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.IT);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.FUN);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.ENTERTAINMENT);
        verify(viewMock, times(1)).addNavigationCategoryButton(Category.GAME);
        verify(viewMock, times(1)).refreshShowCategoryAndType(Category.MAIN, Type.HOT, 0);
    }

    @Test
    public void onStartTest() {
        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        presenter.onStart();
        verify(viewMock, times(1)).removeNavigationAdditions();
        verify(viewMock, never()).addAdditionKeyword(anyString());
        verify(viewMock, never()).addAdditionUser(anyString());
        verify(viewMock, times(1)).disableUserInfo();
        verify(viewMock, never()).enableUserInfo(anyString(), anyString());
    }

    @Test
    public void toolbarClickTest() {
        when(viewMock.isOpenNavigation()).thenReturn(false);

        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        presenter.onStart();
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

    @Test
    public void typeSelectTest() {
        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        presenter.onStart();
        Assert.assertEquals(presenter.getCurrentType(), Type.HOT);
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(Category.class), eq(Type.HOT), eq(0));
        verify(viewMock, never()).refreshShowCategoryAndType(any(Category.class), eq(Type.NEW), eq(1));

        presenter.onNavigationClick(1);
        Assert.assertEquals(presenter.getCurrentType(), Type.NEW);
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(Category.class), eq(Type.HOT), eq(0));
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(Category.class), eq(Type.NEW), eq(1));

        presenter.onNavigationClick(0);
        Assert.assertEquals(presenter.getCurrentType(), Type.HOT);
        verify(viewMock, times(2)).refreshShowCategoryAndType(any(Category.class), eq(Type.HOT), eq(0));
        verify(viewMock, times(1)).refreshShowCategoryAndType(any(Category.class), eq(Type.NEW), eq(1));
    }

    @Test
    public void backKeyPressTest() {
        when(viewMock.isOpenNavigation()).thenReturn(false);

        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        presenter.onStart();
        presenter.onBackKeyClick();
        verify(viewMock, times(1)).backPress();
        verify(viewMock, never()).closeNavigation();

        when(viewMock.isOpenNavigation()).thenReturn(true);
        presenter.onBackKeyClick();
        verify(viewMock, times(1)).backPress();
        verify(viewMock, times(1)).closeNavigation();
    }

    @Test
    public void additionUserTest() {
        List<String> users = new ArrayList<>();
        users.add("testA");
        users.add("testB");
        users.add("testC");
        when(useCaseMock.getAdditionUsers()).thenReturn(users);

        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        presenter.onStart();
        verify(useCaseMock, times(1)).getAdditionUsers();
        verify(viewMock, times(1)).addAdditionUser(users.get(0));
        verify(viewMock, times(1)).addAdditionUser(users.get(1));
        verify(viewMock, times(1)).addAdditionUser(users.get(2));

        View testBView = mock(View.class);
        presenter.onAdditionUserClick(users.get(1));
        verify(viewMock, times(1)).startActivity(eq(UserSearchActivity.class), any(Bundle.class));
        verify(viewMock, times(1)).closeNavigation();

        presenter.onAdditionUserLongClick(users.get(1), testBView);
        verify(viewMock, times(1)).showDeleteUserDialog(users.get(1), testBView);
        verify(viewMock, never()).showDeleteUserDialog(eq(users.get(0)), any(View.class));
        verify(viewMock, never()).showDeleteUserDialog(eq(users.get(2)), any(View.class));
        verify(useCaseMock, never()).deleteAdditionUser(users.get(1));

        presenter.onDeleteUserIdDialogClick(users.get(1), testBView);
        verify(useCaseMock, times(1)).deleteAdditionUser(users.get(1));
        verify(useCaseMock, never()).deleteAdditionUser(users.get(0));
        verify(useCaseMock, never()).deleteAdditionUser(users.get(2));
        verify(testBView, times(1)).setVisibility(View.GONE);
    }

    @Test
    public void additionKeywordTest() {
        List<String> keywords = new ArrayList<>();
        keywords.add("testA");
        keywords.add("testB");
        keywords.add("testC");
        when(useCaseMock.getAdditionKeywords()).thenReturn(keywords);

        TopPresenter presenter = new TopPresenter(viewMock, useCaseMock, Category.MAIN, Type.HOT);
        presenter.onCreate();
        presenter.onStart();
        verify(useCaseMock, times(1)).getAdditionKeywords();
        verify(viewMock, times(1)).addAdditionKeyword(keywords.get(0));
        verify(viewMock, times(1)).addAdditionKeyword(keywords.get(1));
        verify(viewMock, times(1)).addAdditionKeyword(keywords.get(2));

        View testBView = mock(View.class);
        presenter.onAdditionKeywordClick(keywords.get(1));
        verify(viewMock, times(1)).startActivity(eq(KeywordSearchActivity.class), any(Bundle.class));
        verify(viewMock, times(1)).closeNavigation();

        presenter.onAdditionKeywordLongClick(keywords.get(1), testBView);
        verify(viewMock, times(1)).showDeleteKeywordDialog(keywords.get(1), testBView);
        verify(viewMock, never()).showDeleteKeywordDialog(eq(keywords.get(0)), any(View.class));
        verify(viewMock, never()).showDeleteKeywordDialog(eq(keywords.get(2)), any(View.class));
        verify(useCaseMock, never()).deleteAdditionKeyword(keywords.get(1));

        presenter.onDeleteKeywordDialogClick(keywords.get(1), testBView);
        verify(useCaseMock, times(1)).deleteAdditionKeyword(keywords.get(1));
        verify(useCaseMock, never()).deleteAdditionKeyword(keywords.get(0));
        verify(useCaseMock, never()).deleteAdditionKeyword(keywords.get(2));
        verify(testBView, times(1)).setVisibility(View.GONE);
    }
}
