package me.kirimin.mitsumine.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.squareup.picasso.Picasso

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.network.api.EntryInfoApi
import me.kirimin.mitsumine.ui.fragment.BookmarkListFragment
import me.kirimin.mitsumine.ui.fragment.RegisterBookmarkFragment
import me.kirimin.mitsumine.ui.adapter.EntryInfoPagerAdapter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import kotlinx.android.synthetic.activity_entry_info.*

public class EntryInfoActivity : AppCompatActivity() {

    companion object {
        public fun buildBundle(url: String): Bundle {
            val bundle = Bundle()
            bundle.putString("url", url)
            return bundle
        }
    }

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_info)
        setSupportActionBar(toolBar as Toolbar)
        val actionBar = supportActionBar
        actionBar.setTitle(R.string.entry_info_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        val url = getIntent().getStringExtra("url")
        if (url == null) {
            finish()
        }
        subscriptions.add(EntryInfoApi.request(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { entryInfo -> !entryInfo.isNullObject() }
                .subscribe ({ entryInfo ->
                    countLayout.visibility = View.VISIBLE
                    titleTextView.text = entryInfo.title
                    bookmarkCountTextView.text = entryInfo.bookmarkCount.toString()
                    Picasso.with(applicationContext).load(entryInfo.thumbnailUrl).fit().into(thumbnailImageView)
                    tagsText.text = entryInfo.tagList.joinToString(", ")

                    val adapter = EntryInfoPagerAdapter(supportFragmentManager)
                    adapter.addPage(BookmarkListFragment.newFragment(entryInfo.bookmarkList), getString(R.string.entry_info_all_bookmarks))
                    subscriptions.add(Observable.from<Bookmark>(entryInfo.bookmarkList)
                            .filter { bookmark -> bookmark.hasComment() }
                            .toList()
                            .subscribe { commentList ->
                                commentCountTextView.text = commentList.size().toString()
                                adapter.addPage(BookmarkListFragment.newFragment(commentList), getString(R.string.entry_info_comments))
                                AccountDAO.get()?.let {
                                    adapter.addPage(RegisterBookmarkFragment.newFragment(entryInfo.url), getString(R.string.entry_info_register_bookmark))
                                }
                                commentsViewPager.adapter = adapter
                                commentsViewPager.currentItem = 1
                                commentsViewPager.offscreenPageLimit = 2
                                tabs.setViewPager(commentsViewPager)
                            })
                }, { Toast.makeText(applicationContext, R.string.network_error, Toast.LENGTH_SHORT).show() })
        )
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
