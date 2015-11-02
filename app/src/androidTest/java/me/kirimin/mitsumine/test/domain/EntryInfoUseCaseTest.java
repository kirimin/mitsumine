package me.kirimin.mitsumine.test.domain;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.domain.usecase.EntryInfoUseCase;
import me.kirimin.mitsumine.model.Bookmark;

@RunWith(AndroidJUnit4.class)
public class EntryInfoUseCaseTest {

    @Test
    public void getHasCommentBookmarkTest(){
        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(new Bookmark("test1", new ArrayList<String>(), "", "comment", ""));
        bookmarks.add(new Bookmark("test2", new ArrayList<String>(), "", "", ""));
        bookmarks.add(new Bookmark("test3", new ArrayList<String>(), "", "comment", ""));
        bookmarks.add(new Bookmark("test4", new ArrayList<String>(), "", "comment", ""));
        EntryInfoUseCase useCase = new EntryInfoUseCase();
        List<Bookmark> results = useCase.getHasCommentBookmarks(bookmarks);
        Assert.assertEquals(results.size(), 3);
        Assert.assertEquals(results.get(0).getUser(), "test1");
        Assert.assertEquals(results.get(1).getUser(), "test3");
        Assert.assertEquals(results.get(1).getUser(), "test4");
    }
}
