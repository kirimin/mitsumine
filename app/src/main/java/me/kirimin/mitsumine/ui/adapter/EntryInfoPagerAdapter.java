package me.kirimin.mitsumine.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.ui.fragment.EntryInfoFragment;

public class EntryInfoPagerAdapter extends FragmentPagerAdapter {

    private final List<Bookmark> allBookmarkList;
    private final List<Bookmark> commentList;
    private final Context context;

    public EntryInfoPagerAdapter(FragmentManager fm, List<Bookmark> allBookmarkList, List<Bookmark> commentList, Context context) {
        super(fm);
        this.allBookmarkList = allBookmarkList;
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return EntryInfoFragment.newFragment(allBookmarkList);
            case 1:
                return EntryInfoFragment.newFragment(commentList);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.entry_info_all_bookmarks);
            case 1:
                return context.getString(R.string.entry_info_comments);
            default:
                throw new IllegalArgumentException();
        }
    }
}
