package me.kirimin.mitsumine.search

import android.support.v4.app.Fragment

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.mybookmark.MyBookmarksFragment

public class MyBookmarksActivity : SearchActivity() {

    override fun newFragment(keyword: String): Fragment {
        return MyBookmarksFragment.newFragment(keyword)
    }

    override fun doFavorite() {
    }

    override fun getSearchTitle(): String {
        return getString(R.string.my_bookmarks_title)
    }
}