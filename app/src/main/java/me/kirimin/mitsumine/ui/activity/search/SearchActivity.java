package me.kirimin.mitsumine.ui.activity.search;

import me.kirimin.mitsumine.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public abstract class SearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private MenuItem mSearchItem;
    private SearchView mSearchView;
    private String mQueryStr = "";
    private boolean mIsShowFavorite = true;

    abstract Fragment newFragment(String keyword);

    abstract void doFavorite();

    abstract String getSearchTitle();

    public static Bundle buildBundle(String keyword) {
        return buildBundle(keyword, true);
    }

    public static Bundle buildBundle(String keyword, boolean isShowFavorite) {
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        bundle.putBoolean("isShowFavorite", isShowFavorite);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String keyword = getIntent().getStringExtra("keyword");
        if (keyword == null) {
            setTitle(getSearchTitle());
            return;
        }
        mQueryStr = keyword;
        mIsShowFavorite = getIntent().getBooleanExtra("isShowFavorite", true);
        setTitle(keyword);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, newFragment(keyword))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        mSearchItem = menu.findItem(R.id.SearchMenuItemSearchView);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchView.setQueryHint(getSearchTitle());
        mSearchView.setOnQueryTextListener(this);
        if (mQueryStr.isEmpty()) {
            mSearchItem.expandActionView();
        }
        if (!mIsShowFavorite) {
            menu.findItem(R.id.SearchMenuItemFavorite).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        } else if (R.id.SearchMenuItemFavorite == item.getItemId() && !mQueryStr.isEmpty()) {
            doFavorite();
        } else if (R.id.SearchMenuItemSearchView == item.getItemId()) {
            mSearchItem.expandActionView();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        setTitle(query);
        mQueryStr = query;
        mSearchItem.collapseActionView();
        MenuItemCompat.setOnActionExpandListener(mSearchItem, this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, newFragment(query))
                .commit();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        return true;
    }
}
