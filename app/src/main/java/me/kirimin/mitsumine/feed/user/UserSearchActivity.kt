package me.kirimin.mitsumine.feed.user

import android.widget.Toast
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.UserIdDAO
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.user.UserSearchFragment
import me.kirimin.mitsumine.search.AbstractSearchActivity

class UserSearchActivity : AbstractSearchActivity() {

    override fun newFragment(keyword: String): AbstractFeedFragment {
        return UserSearchFragment.newFragment(keyword)
    }

    override fun doFavorite() {
        UserIdDAO.save(title.toString())
        Toast.makeText(this, R.string.user_search_toast_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun getSearchTitle(): String {
        return getString(R.string.user_search_title)
    }

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }
}
