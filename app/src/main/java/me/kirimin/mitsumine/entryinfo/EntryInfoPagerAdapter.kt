package me.kirimin.mitsumine.entryinfo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import java.util.ArrayList

class EntryInfoPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getItem(i: Int) = fragmentList[i]

    override fun getCount() = fragmentList.size

    override fun getPageTitle(position: Int) = titleList[position]

    fun addPage(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
