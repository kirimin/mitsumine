package me.kirimin.mitsumine.ui.activity.search;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.KeywordDAO;
import me.kirimin.mitsumine.ui.fragment.AbstractFeedFragment;
import me.kirimin.mitsumine.ui.fragment.KeywordFeedFragment;
import android.widget.Toast;

public class KeywordSearchActivity extends SearchActivity {

    @Override
    AbstractFeedFragment newFragment(String keyword) {
        return KeywordFeedFragment.newFragment(keyword);
    }

    @Override
    void doFavorite() {
        KeywordDAO.save(getTitle().toString());
        Toast.makeText(this, R.string.keyword_search_toast_favorite, Toast.LENGTH_SHORT).show();
    }

    @Override
    String getSearchTitle() {
        return getString(R.string.keyword_search_title);
    }
}
