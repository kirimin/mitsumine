package me.kirimin.mitsumine.top

import android.os.Bundle
import android.view.View
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type

interface TopView {

    fun closeNavigation()

    fun openNavigation()

    fun isOpenNavigation(): Boolean

    fun refreshShowCategoryAndType(category: Category, type: Type, typeInt: Int)

    fun startActivity(activityClass: Class<*>)

    fun startActivity(activityClass: Class<*>, bundle: Bundle)

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
