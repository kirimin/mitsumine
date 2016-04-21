package me.kirimin.mitsumine.search

import me.kirimin.mitsumine.R

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager

import kotlinx.android.synthetic.main.activity_common_container.*

abstract class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {

    private lateinit var mSearchItem: MenuItem
    private lateinit var mSearchView: SearchView
    private var mQueryStr = ""
    private var mIsShowFavorite = true

    abstract fun newFragment(keyword: String): Fragment

    abstract fun doFavorite()

    abstract fun getSearchTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_container)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        val keyword = intent.getStringExtra("keyword")
        if (keyword == null) {
            title = getSearchTitle()
            return
        }
        mQueryStr = keyword
        mIsShowFavorite = getIntent().getBooleanExtra("isShowFavorite", true)
        title = keyword
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerFrameLayout, newFragment(keyword))
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        mSearchItem = menu.findItem(R.id.SearchMenuItemSearchView)
        mSearchView = MenuItemCompat.getActionView(mSearchItem) as SearchView
        mSearchView.queryHint = getSearchTitle()
        mSearchView.setOnQueryTextListener(this)
        if (mQueryStr.isEmpty()) {
            mSearchItem.expandActionView()
        }
        if (!mIsShowFavorite) {
            menu.findItem(R.id.SearchMenuItemFavorite).setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        } else if (R.id.SearchMenuItemFavorite == item.itemId && !mQueryStr.isEmpty()) {
            doFavorite()
        } else if (R.id.SearchMenuItemSearchView == item.itemId) {
            mSearchItem.expandActionView()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        title = query
        mQueryStr = query
        mSearchItem.collapseActionView()
        MenuItemCompat.setOnActionExpandListener(mSearchItem, this)

        supportFragmentManager.beginTransaction().replace(R.id.containerFrameLayout, newFragment(query)).commit()
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
        inputMethodManager.hideSoftInputFromWindow(mSearchView.windowToken, 0)
        return true
    }

    companion object {

        fun buildBundle(keyword: String, isShowFavorite: Boolean = true): Bundle {
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            bundle.putBoolean("isShowFavorite", isShowFavorite)
            return bundle
        }
    }
}
