package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.ui.BaseViewInterface

interface EntryInfoView: BaseViewInterface {

    fun initActionBar()

    fun setEntryInfo(entryInfo: EntryInfo)

    fun setBookmarkFragments(allList: List<Bookmark>, hasCommentList: List<Bookmark>, entryId: String)

    fun setRegisterBookmarkFragment(url: String)

    fun setViewPagerSettings(currentItem: Int, offscreenPageLimit: Int)

    fun setCommentCount(commentCount: String)

    fun showProgressBar()

    fun hideProgressBar()
}
