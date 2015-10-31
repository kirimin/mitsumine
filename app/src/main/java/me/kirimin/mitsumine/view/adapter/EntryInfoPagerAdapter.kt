package me.kirimin.mitsumine.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import java.util.ArrayList

public class EntryInfoPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getItem(i: Int): Fragment {
        return fragmentList.get(i)
    }

    override fun getCount(): Int {
        return fragmentList.size()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleList.get(position)
    }

    public fun addPage(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
