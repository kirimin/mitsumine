package me.kirimin.mitsumine.presenter

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.view.TopView
import me.kirimin.mitsumine.view.activity.LoginActivity
import me.kirimin.mitsumine.view.activity.ReadActivity
import me.kirimin.mitsumine.view.activity.SettingActivity
import me.kirimin.mitsumine.view.activity.TopActivity
import me.kirimin.mitsumine.view.activity.search.KeywordSearchActivity
import me.kirimin.mitsumine.view.activity.search.MyBookmarksActivity
import me.kirimin.mitsumine.view.activity.search.UserSearchActivity

class TopPresenter {

    private var view: TopView? = null

    private var mSelectedType = FeedApi.TYPE.HOT

    fun takeView(topView: TopView) {
        view = topView

        // 古い既読を削除
        FeedDAO.deleteOldData()
        view!!.refreshShowCategoryAndType(mSelectedType)
    }

    fun onNavigationClick(position: Int, id: Long) {
        view!!.closeNavigation()
        mSelectedType = if (position == 0) FeedApi.TYPE.HOT else FeedApi.TYPE.NEW
        view!!.refreshShowCategoryAndType(mSelectedType)
    }

    fun onToolbarClick() {
        if (view!!.isOpenNavigation) {
            view!!.closeNavigation()
        } else {
            view!!.openNavigation()
        }
    }

    fun onViewClick(id: Int) {
        when (id) {
            R.id.navigationReadTextView -> {
                view!!.startActivity(ReadActivity::class.java)
                view!!.closeNavigation()
            }
            R.id.navigationSettingsTextView -> {
                view!!.startActivity(SettingActivity::class.java)
                view!!.closeNavigation()
            }
            R.id.navigationKeywordSearchTextView -> {
                view!!.startActivity(KeywordSearchActivity::class.java)
                view!!.closeNavigation()
            }
            R.id.navigationUserSearchTextView -> {
                view!!.startActivity(UserSearchActivity::class.java)
                view!!.closeNavigation()
            }
            R.id.navigationUserInfoLayout -> {
                view!!.startActivity(MyBookmarksActivity::class.java)
                view!!.closeNavigation()
            }
            R.id.navigationLoginButton -> {
                view!!.startActivity(LoginActivity::class.java)
            }
        }
    }
}
