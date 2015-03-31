package me.kirimin.mitsumine.ui.activity.search

import me.kirimin.mitsumine.R

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager

import kotlinx.android.synthetic.activity_common_container.*

public abstract class SearchActivity : ActionBarActivity(), SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private var mSearchItem: MenuItem? = null
    private var mSearchView: SearchView? = null
    private var mQueryStr = ""
    private var mIsShowFavorite = true

    abstract fun newFragment(keyword: String): Fragment

    abstract fun doFavorite()

    abstract fun getSearchTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super<ActionBarActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_container)
        setSupportActionBar(toolBar)
        val actionBar = getSupportActionBar()
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        val keyword = getIntent().getStringExtra("keyword")
        if (keyword == null) {
            setTitle(getSearchTitle())
            return
        }
        mQueryStr = keyword
        mIsShowFavorite = getIntent().getBooleanExtra("isShowFavorite", true)
        setTitle(keyword)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFrameLayout, newFragment(keyword))
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.search, menu)

        mSearchItem = menu.findItem(R.id.SearchMenuItemSearchView)
        mSearchView = MenuItemCompat.getActionView(mSearchItem) as SearchView
        mSearchView!!.setQueryHint(getSearchTitle())
        mSearchView!!.setOnQueryTextListener(this)
        if (mQueryStr.isEmpty()) {
            mSearchItem!!.expandActionView()
        }
        if (!mIsShowFavorite) {
            menu.findItem(R.id.SearchMenuItemFavorite).setVisible(false)
        }
        return super<ActionBarActivity>.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        } else if (R.id.SearchMenuItemFavorite == item.getItemId() && !mQueryStr.isEmpty()) {
            doFavorite()
        } else if (R.id.SearchMenuItemSearchView == item.getItemId()) {
            mSearchItem!!.expandActionView()
        }
        return super<ActionBarActivity>.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        setTitle(query)
        mQueryStr = query
        mSearchItem!!.collapseActionView()
        MenuItemCompat.setOnActionExpandListener(mSearchItem, this)

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, newFragment(query)).commit()
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mSearchView!!.getWindowToken(), 0)
        return true
    }

    companion object {

        public fun buildBundle(keyword: String): Bundle {
            return buildBundle(keyword, true)
        }

        public fun buildBundle(keyword: String, isShowFavorite: Boolean): Bundle {
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            bundle.putBoolean("isShowFavorite", isShowFavorite)
            return bundle
        }
    }
}
