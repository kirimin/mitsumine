package me.kirimin.mitsumine.bookmarklist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.domain.model.Bookmark
import me.kirimin.mitsumine.search.SearchActivity
import me.kirimin.mitsumine.search.UserSearchActivity

import kotlinx.android.synthetic.main.fragment_bookmark_list.view.*

class BookmarkListFragment : Fragment(), BookmarkListView {

    companion object {
        fun newFragment(bookmarkList: List<Bookmark>): BookmarkListFragment {
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
        presenter.onCreate(this, arguments.getParcelableArray("bookmarkList").map { it as Bookmark })
    }

    override fun initViews(bookmarks: List<Bookmark>) {
        val view = view ?: return
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
