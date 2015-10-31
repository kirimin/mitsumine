package me.kirimin.mitsumine.view.activity.search

import android.support.v4.app.Fragment

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.view.fragment.MyBookmarksFragment

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