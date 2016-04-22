package me.kirimin.mitsumine.top

import android.view.View
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type

interface TopView {

    fun closeNavigation()

    fun openNavigation()

    fun isOpenNavigation(): Boolean

    fun refreshShowCategoryAndType(category: Category, type: Type)

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

    fun startUserSearchActivity(userId: String = "")

    fun startKeywordSearchActivity(keyword: String = "")

    fun startMyBookmarksActivity()

    fun startReadActivity()

    fun startSettingActivity()

    fun startLoginActivity()
}
