package me.kirimin.mitsumine.entryinfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.squareup.picasso.Picasso

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine.bookmarklist.BookmarkListFragment
import me.kirimin.mitsumine.registerbookmark.RegisterBookmarkFragment

import kotlinx.android.synthetic.main.activity_entry_info.*
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine._common.ui.BaseActivity

class EntryInfoActivity : BaseActivity(), EntryInfoView {

    private val presenter = EntryInfoPresenter()
    private val adapter = EntryInfoPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_info)

        val url = intent.getStringExtra(KEY_URL) ?: let { finish(); return }
        presenter.onCreate(this, EntryInfoUseCase(), url)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initActionBar() {
        setSupportActionBar(toolBar as Toolbar)
        val actionBar = supportActionBar ?: return
        actionBar.setTitle(R.string.entry_info_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
    }

    override fun setBookmarkFragments(allList: List<Bookmark>, hasCommentList: List<Bookmark>, entryId: String) {
        adapter.addPage(BookmarkListFragment.newFragment(allList, entryId), getString(R.string.entry_info_all_bookmarks))
        adapter.addPage(BookmarkListFragment.newFragment(hasCommentList, entryId), getString(R.string.entry_info_comments))
    }

    override fun setRegisterBookmarkFragment(url: String) {
        adapter.addPage(RegisterBookmarkFragment.newFragment(url), getString(R.string.entry_info_register_bookmark))
    }

    override fun setEntryInfo(entryInfo: EntryInfo) {
        countLayout.visibility = View.VISIBLE
        titleTextView.text = entryInfo.title
        bookmarkCountTextView.text = entryInfo.bookmarkCount.toString()
        Picasso.with(applicationContext).load(entryInfo.thumbnailUrl).fit().into(thumbnailImageView)
        tagsText.text = entryInfo.tagListString
    }

    override fun setViewPagerSettings(currentItem: Int, offscreenPageLimit: Int) {
        commentsViewPager.adapter = adapter
        commentsViewPager.currentItem = currentItem
        commentsViewPager.offscreenPageLimit = offscreenPageLimit
        tabs.setViewPager(commentsViewPager)
    }

    override fun setCommentCount(commentCount: String) {
        commentCountTextView.text = commentCount
    }

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }

    companion object {
        val KEY_URL = "url"

        fun buildBundle(url: String): Bundle {
            val bundle = Bundle()
            bundle.putString(KEY_URL, url)
            return bundle
        }
    }

}
