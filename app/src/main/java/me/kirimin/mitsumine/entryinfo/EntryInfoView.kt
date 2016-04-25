package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.domain.model.EntryInfo

interface EntryInfoView {

    fun initActionBar()

    fun setEntryInfo(entryInfo: EntryInfo)

    fun setBookmarkFragments(allList: List<Bookmark>, hasCommentList: List<Bookmark>)

    fun setRegisterBookmarkFragment(url: String)

    fun setViewPagerSettings(currentItem: Int, offscreenPageLimit: Int)

    fun setCommentCount(commentCount: String)

    fun showNetworkErrorToast()

}
