package me.kirimin.mitsumine.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.domain.model.Bookmark
import me.kirimin.mitsumine.view.activity.search.SearchActivity
import me.kirimin.mitsumine.view.activity.search.UserSearchActivity
import me.kirimin.mitsumine.view.adapter.BookmarkListAdapter

import me.kirimin.mitsumine.presenter.BookmarkListPresenter
import me.kirimin.mitsumine.view.BookmarkListView

import kotlinx.android.synthetic.fragment_bookmark_list.view.*

public class BookmarkListFragment : Fragment(), BookmarkListView {

    companion object {
        public fun newFragment(bookmarkList: List<Bookmark>): BookmarkListFragment {
            val fragment = BookmarkListFragment()
            val bundle = Bundle()
            bundle.putParcelableArray("bookmarkList", bookmarkList.toTypedArray())
            fragment.arguments = bundle
            return fragment
        }
    }

    val presenter = BookmarkListPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_bookmark_list, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.onCreate(this, arguments.getParcelableArray("bookmarkList").map { o -> o as Bookmark })
    }

    override fun initViews(bookmarks: List<Bookmark>) {
        val adapter = BookmarkListAdapter(activity, presenter)
        adapter.addAll(bookmarks)
        view.listView.adapter = adapter
    }

    override fun startUserSearchActivity(userId: String) {
        val intent = Intent(activity, UserSearchActivity::class.java)
        intent.putExtras(SearchActivity.buildBundle(userId))
        startActivity(intent)
    }
}
