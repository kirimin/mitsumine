package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.domain.model.Bookmark
import java.util.*


interface RegisterBookmarkView {

    fun showViewWithoutBookmarkInfo()
    fun showViewWithBookmarkInfo(bookmark: Bookmark)
    fun showErrorToast()
    fun initView()
    fun disableButtons()
    fun getViewStatus(): Triple<String, Boolean, Boolean>

    fun showRegisterToast()

    fun showDeletedToast()

    fun getTagsText(): String

    fun showTagEditDialog(tags: ArrayList<String>)
    fun updateCommentCount(length: Int)
}