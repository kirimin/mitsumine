package me.kirimin.mitsumine.search

import android.widget.Toast
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.database.UserIdDAO
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.search.UserFeedFragment

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
