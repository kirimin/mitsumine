package me.kirimin.mitsumine.top

import android.view.View
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type

class TopPresenter {

    private var view: TopView? = null
    private lateinit var repository: TopRepository

    var selectedType = Type.HOT
        private set
    var selectedCategory = Category.MAIN
        private set

    fun getTypeInt(type: Type) = if (type == Type.HOT) 0 else 1

    fun onCreate(view: TopView, repository: TopRepository, category: Category = Category.MAIN, type: Type = Type.HOT) {
        this.view = view
        this.repository = repository
        selectedCategory = category
        selectedType = type

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
        repository.deleteOldFeedData(3)
        view.refreshShowCategoryAndType(selectedCategory, selectedType)
    }

    fun onStart() {
        val view = view ?: return
        view.removeNavigationAdditions()
        repository.additionKeywords.forEach { keyword -> view.addAdditionKeyword(keyword) }
        repository.additionUsers.forEach { userId -> view.addAdditionUser(userId) }

        val account = repository.account
        if (account != null) {
            view.enableUserInfo(account.displayName, account.imageUrl)
        } else {
            view.disableUserInfo()
        }
    }

    fun onDestroy() {
        view = null
    }

    fun onNavigationClick(position: Int): Boolean {
        selectedType = if (position == 0) Type.HOT else Type.NEW
        view?.closeNavigation()
        view?.refreshShowCategoryAndType(selectedCategory, selectedType)
        return true
    }

    fun onToolbarClick() {
        val view = view ?: return
        if (view.isOpenNavigation()) {
            view.closeNavigation()
        } else {
            view.openNavigation()
        }
    }

    fun onBackKeyClick() {
        val view = view ?: return
        if (view.isOpenNavigation()) {
            view.closeNavigation()
        } else {
            view.backPress()
        }
    }

    fun onNavigationItemClick(id: Int) {
        val view = view ?: return
        when (id) {
            R.id.navigationReadTextView -> {
                view.startReadActivity()
                view.closeNavigation()
            }
            R.id.navigationSettingsTextView -> {
                view.startSettingActivity()
                view.closeNavigation()
            }
            R.id.navigationKeywordSearchTextView -> {
                view.startKeywordSearchActivity()
                view.closeNavigation()
            }
            R.id.navigationUserSearchTextView -> {
                view.startUserSearchActivity()
                view.closeNavigation()
            }
            R.id.navigationUserInfoLayout -> {
                view.startMyBookmarksActivity()
                view.closeNavigation()
            }
            R.id.navigationLoginButton -> {
                view.startLoginActivity()
                view.closeNavigation()
            }
        }
    }

    fun onCategoryClick(category: Category) {
        view?.closeNavigation()
        view?.refreshShowCategoryAndType(category, selectedType)
        selectedCategory = category
    }

    fun onAdditionUserClick(userId: String) {
        view?.closeNavigation()
        view?.startUserSearchActivity(userId)
    }

    fun onAdditionUserLongClick(userId: String, target: View): Boolean {
        view?.showDeleteUserDialog(userId, target)
        return false
    }

    fun onAdditionKeywordClick(keyword: String) {
        view?.closeNavigation()
        view?.startKeywordSearchActivity(keyword)
    }

    fun onAdditionKeywordLongClick(keyword: String, target: View): Boolean {
        view?.showDeleteKeywordDialog(keyword, target)
        return false
    }

    fun onDeleteUserIdDialogClick(word: String, target: View) {
        repository.deleteAdditionUser(word)
        target.visibility = View.GONE
    }

    fun onDeleteKeywordDialogClick(word: String, target: View) {
        repository.deleteAdditionKeyword(word)
        target.visibility = View.GONE
    }
}
