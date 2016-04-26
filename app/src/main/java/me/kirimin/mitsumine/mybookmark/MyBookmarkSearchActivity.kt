package me.kirimin.mitsumine.mybookmark

import android.support.v4.app.Fragment

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.search.AbstractSearchActivity

class MyBookmarkSearchActivity : AbstractSearchActivity() {

    override fun newFragment(keyword: String): Fragment {
        return MyBookmarkSearchFragment.newFragment(keyword)
    }

    override fun doFavorite() {
    }

    override fun getSearchTitle(): String {
        return getString(R.string.my_bookmarks_title)
    }
}