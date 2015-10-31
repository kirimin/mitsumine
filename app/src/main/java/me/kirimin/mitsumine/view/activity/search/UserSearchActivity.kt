package me.kirimin.mitsumine.view.activity.search

import android.widget.Toast
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.UserIdDAO
import me.kirimin.mitsumine.view.fragment.AbstractFeedFragment
import me.kirimin.mitsumine.view.fragment.UserFeedFragment

public class UserSearchActivity : SearchActivity() {

    override fun newFragment(keyword: String): AbstractFeedFragment {
        return UserFeedFragment.newFragment(keyword)
    }

    override fun doFavorite() {
        UserIdDAO.save(title.toString())
        Toast.makeText(this, R.string.user_search_toast_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun getSearchTitle(): String {
        return getString(R.string.user_search_title)
    }
}
