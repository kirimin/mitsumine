package me.kirimin.mitsumine.ui.activity.search;

import android.widget.Toast;
import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.UserIdDAO;
import me.kirimin.mitsumine.ui.fragment.AbstractFeedFragment;
import me.kirimin.mitsumine.ui.fragment.UserFeedFragment;

public class UserSearchActivity extends SearchActivity {

    @Override
    AbstractFeedFragment newFragment(String keyword) {
        return UserFeedFragment.newFragment(keyword);
    }

    @Override
    void doFavorite() {
        UserIdDAO.save(getTitle().toString());
        Toast.makeText(this, R.string.user_search_toast_favorite, Toast.LENGTH_SHORT).show();
    }

    @Override
    String getSearchTitle() {
        return getString(R.string.user_search_title);
    }
}
