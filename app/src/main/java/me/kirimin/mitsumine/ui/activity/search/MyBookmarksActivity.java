package me.kirimin.mitsumine.ui.activity.search;

import android.support.v4.app.Fragment;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.ui.fragment.MyBookmarksFragment;

public class MyBookmarksActivity extends SearchActivity {

    @Override
    public Fragment newFragment(String keyword) {
        return MyBookmarksFragment.newFragment(keyword);
    }

    @Override
    void doFavorite() {
    }

    @Override
    String getSearchTitle() {
        return getString(R.string.my_bookmarks_title);
    }
}