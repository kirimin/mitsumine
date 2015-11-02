package me.kirimin.mitsumine.view

import android.support.v4.app.Fragment
import me.kirimin.mitsumine.model.EntryInfo

interface EntryInfoView {

    fun initActionBar()

    fun setEntryInfo(entryInfo: EntryInfo)

    fun addPage(fragment: Fragment, title: Int)

    fun setViewPagerSettings(currentItem: Int, offscreenPageLimit: Int)

    fun setCommentCount(commentCount: String)

    fun showNetworkErrorToast()

}
