package me.kirimin.mitsumine.test;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.util.EntryInfoFunc;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EntryInfoFuncTest {

    @Test
    public void hasCommentはコメントが空文字じゃないかを判定() {
        Bookmark bookmark1 = new Bookmark("user", new ArrayList<String>(), "timestamp", "comment", "icon");
        Bookmark bookmark2 = new Bookmark("user", new ArrayList<String>(), "timestamp", "コメント", "icon");
        Bookmark bookmark3 = new Bookmark("user", new ArrayList<String>(), "timestamp", " ", "icon");
        Bookmark bookmark4 = new Bookmark("user", new ArrayList<String>(), "timestamp", "", "icon");
        assertThat(EntryInfoFunc.Companion.hasComment(bookmark1), is(true));
        assertThat(EntryInfoFunc.Companion.hasComment(bookmark2), is(true));
        assertThat(EntryInfoFunc.Companion.hasComment(bookmark3), is(true));
        assertThat(EntryInfoFunc.Companion.hasComment(bookmark4), is(false));
    }
}
