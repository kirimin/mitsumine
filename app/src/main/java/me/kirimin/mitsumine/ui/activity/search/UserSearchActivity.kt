package me.kirimin.mitsumine.ui.activity.search

import android.widget.Toast
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.UserIdDAO
import me.kirimin.mitsumine.ui.fragment.AbstractFeedFragment
import me.kirimin.mitsumine.ui.fragment.UserFeedFragment

public class UserSearchActivity : SearchActivity() {

    override fun newFragment(keyword: String): AbstractFeedFragment {
        return UserFeedFragment.newFragment(keyword)
    }

    override fun doFavorite() {
        UserIdDAO.save(getTitle().toString())
        Toast.makeText(this, R.string.user_search_toast_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun getSearchTitle(): String {
        return getString(R.string.user_search_title)
    }
}
