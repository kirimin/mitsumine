package me.kirimin.mitsumine.presenter

import android.view.View
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.domain.usecase.TopUseCase
import me.kirimin.mitsumine.model.Category
import me.kirimin.mitsumine.model.Type
import me.kirimin.mitsumine.view.TopView
import me.kirimin.mitsumine.view.activity.LoginActivity
import me.kirimin.mitsumine.view.activity.ReadActivity
import me.kirimin.mitsumine.view.activity.SettingActivity
import me.kirimin.mitsumine.view.activity.search.KeywordSearchActivity
import me.kirimin.mitsumine.view.activity.search.MyBookmarksActivity
import me.kirimin.mitsumine.view.activity.search.UserSearchActivity

class TopPresenter {

    private val view: TopView
    private val useCase: TopUseCase

    private var mSelectedType = Type.HOT
    private var mSelectedCategory = Category.MAIN

    constructor(topView: TopView, topUseCase: TopUseCase, category: Category = Category.MAIN, type: Type = Type.HOT) {
        view = topView
        useCase = topUseCase
        mSelectedCategory = category
        mSelectedType = type
    }

    fun onCreate() {
        view.initViews()

        view.addNavigationCategoryButton(Category.MAIN)
        view.addNavigationCategoryButton(Category.SOCIAL)
        view.addNavigationCategoryButton(Category.ECONOMICS)
        view.addNavigationCategoryButton(Category.LIFE)
        view.addNavigationCategoryButton(Category.KNOWLEDGE)
        view.addNavigationCategoryButton(Category.IT)
        view.addNavigationCategoryButton(Category.FUN)
        view.addNavigationCategoryButton(Category.ENTERTAINMENT)
        view.addNavigationCategoryButton(Category.GAME)

        // 古い既読を削除
        useCase.deleteOldFeedData()
        view.refreshShowCategoryAndType(mSelectedCategory, mSelectedType, getTypeInt(mSelectedType))
    }

    fun onStart() {
        view.removeNavigationAdditions()
        useCase.additionKeywords.forEach { keyword -> view.addAdditionKeyword(keyword) }
        useCase.additionUsers.forEach { userId -> view.addAdditionUser(userId) }

        val account = useCase.account
        if (account != null) {
            view.enableUserInfo(account.displayName, account.imageUrl)
        } else {
            view.disableUserInfo()
        }
    }

    fun onNavigationClick(position: Int) {
        view.closeNavigation()
        mSelectedType = if (position == 0) Type.HOT else Type.NEW
        view.refreshShowCategoryAndType(mSelectedCategory, mSelectedType, getTypeInt(mSelectedType))
    }

    fun onToolbarClick() {
        if (view.isOpenNavigation()) {
            view.closeNavigation()
        } else {
            view.openNavigation()
        }
    }

    fun onBackKeyClick() {
        if (view.isOpenNavigation()) {
            view.closeNavigation()
        } else {
            view.backPress()
        }
    }

    fun onViewClick(id: Int) {
        when (id) {
            R.id.navigationReadTextView -> {
                view.startActivity(ReadActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationSettingsTextView -> {
                view.startActivity(SettingActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationKeywordSearchTextView -> {
                view.startActivity(KeywordSearchActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationUserSearchTextView -> {
                view.startActivity(UserSearchActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationUserInfoLayout -> {
                view.startActivity(MyBookmarksActivity::class.java)
                view.closeNavigation()
            }
            R.id.navigationLoginButton -> {
                view.startActivity(LoginActivity::class.java)
            }
        }
    }

    fun onCategoryClick(category: Category) {
        view.closeNavigation()
        view.refreshShowCategoryAndType(category, mSelectedType, getTypeInt(mSelectedType))
        mSelectedCategory = category
    }

    fun onAdditionUserLongClick(userId: String, target: View): Boolean {
        view.showDeleteUserDialog(userId, target)
        return false
    }

    fun onAdditionKeywordLongClick(keyword: String, target: View): Boolean {
        view.showDeleteKeywordDialog(keyword, target)
        return false
    }

    fun onDeleteUserIdDialogClick(word: String, target: View) {
        useCase.deleteAdditionUser(word)
        target.visibility = View.GONE
    }

    fun onDeleteKeywordDialogClick(word: String, target: View) {
        useCase.deleteAdditionKeyword(word)
        target.visibility = View.GONE
    }

    fun getCurrentType(): Type {
        return mSelectedType
    }

    fun getCurrentCategory(): Category {
        return mSelectedCategory
    }

    private fun getTypeInt(type: Type): Int {
        return if (type == Type.HOT) 0 else 1
    }
}
