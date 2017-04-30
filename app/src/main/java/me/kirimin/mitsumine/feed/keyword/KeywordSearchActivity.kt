package me.kirimin.mitsumine.feed.keyword

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.KeywordDAO
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.keyword.KeywordSearchFragment
import android.widget.Toast
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine.search.AbstractSearchActivity

class KeywordSearchActivity : AbstractSearchActivity() {

    override fun newFragment(keyword: String): AbstractFeedFragment {
        return KeywordSearchFragment.newFragment(keyword)
    }

    override fun doFavorite() {
        KeywordDAO.save(title.toString())
        Toast.makeText(this, R.string.keyword_search_toast_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun getSearchTitle(): String {
        return getString(R.string.keyword_search_title)
    }

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }
}
