package me.kirimin.mitsumine.view

import android.view.View
import me.kirimin.mitsumine.model.enums.Category
import me.kirimin.mitsumine.model.enums.Type

interface TopView {

    fun closeNavigation()

    fun openNavigation()

    fun isOpenNavigation(): Boolean

    fun refreshShowCategoryAndType(category: Category, type: Type, typeInt: Int)

    fun startActivity(activityClass: Class<*>)

    fun backPress()

    fun enableUserInfo(userName: String, iconUrl: String)

    fun disableUserInfo()

    fun removeNavigationAdditions()

    fun addAdditionKeyword(keyword: String)

    fun addAdditionUser(userId: String)

    fun addNavigationCategoryButton(category: Category)

    fun initViews()

    fun showDeleteUserDialog(userId: String, view: View)

    fun showDeleteKeywordDialog(keyword: String, view: View)
}
