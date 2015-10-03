package me.kirimin.mitsumine.ui.activity.search

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.KeywordDAO
import me.kirimin.mitsumine.ui.fragment.AbstractFeedFragment
import me.kirimin.mitsumine.ui.fragment.KeywordFeedFragment
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
