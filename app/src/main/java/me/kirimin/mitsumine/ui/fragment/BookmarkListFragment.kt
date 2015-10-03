package me.kirimin.mitsumine.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.ui.activity.search.SearchActivity
import me.kirimin.mitsumine.ui.activity.search.UserSearchActivity
import me.kirimin.mitsumine.ui.adapter.BookmarkListAdapter

import kotlinx.android.synthetic.fragment_bookmark_list.view.*

public class BookmarkListFragment : Fragment() {

    companion object {

        public fun newFragment(bookmarkList: List<Bookmark>): BookmarkListFragment {
            val fragment = BookmarkListFragment()
            val bundle = Bundle()
            bundle.putParcelableArray("bookmarkList", bookmarkList.toTypedArray())
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bookmarks = arguments.getParcelableArray("bookmarkList").map { o -> o as Bookmark }
        val rootView = inflater.inflate(R.layout.fragment_bookmark_list, container, false)
        val adapter = BookmarkListAdapter(activity, { v, bookmark ->
            val intent = Intent(activity, UserSearchActivity::class.java)
            intent.putExtras(SearchActivity.buildBundle(bookmark.user))
            startActivity(intent)
        })
        rootView.listView.adapter = adapter
        adapter.addAll(bookmarks)
        return rootView
    }
}
