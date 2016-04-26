package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.domain.model.Bookmark
import java.util.*


interface RegisterBookmarkView {

    open fun showViewWithoutBookmarkInfo()
    open fun showViewWithBookmarkInfo(bookmark: Bookmark)
    open fun showErrorToast()
    open fun initView()
    fun disableButtons()
    open fun getViewStatus(): Triple<String, Boolean, Boolean>

    fun showRegisterToast()

    fun showDeletedToast()

    fun getTagsText(): String

    open fun showTagEditDialog(tags: ArrayList<String>)
    open fun updateCommentCount(length: Int)
}