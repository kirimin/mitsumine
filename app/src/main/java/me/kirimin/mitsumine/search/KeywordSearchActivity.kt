package me.kirimin.mitsumine.search

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.database.KeywordDAO
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.search.KeywordFeedFragment
import android.widget.Toast

public class KeywordSearchActivity : SearchActivity() {

    override fun newFragment(keyword: String): AbstractFeedFragment {
        return KeywordFeedFragment.newFragment(keyword)
    }

    override fun doFavorite() {
        KeywordDAO.save(title.toString())
        Toast.makeText(this, R.string.keyword_search_toast_favorite, Toast.LENGTH_SHORT).show()
    }

    override fun getSearchTitle(): String {
        return getString(R.string.keyword_search_title)
    }
}
