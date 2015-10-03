package me.kirimin.mitsumine.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.network.api.MyBookmarksApi
import me.kirimin.mitsumine.ui.activity.EntryInfoActivity
import me.kirimin.mitsumine.ui.adapter.MyBookmarksAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.fragment_my_bookmarks.view.*

public class MyBookmarksFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    companion object {

        public fun newFragment(keyword: String): MyBookmarksFragment {
            val fragment = MyBookmarksFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val subscriptions = CompositeSubscription()
    private var adapter: MyBookmarksAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_my_bookmarks, container, false)
        rootView.swipeLayout.setColorSchemeResources(R.color.blue, R.color.orange)
        rootView.swipeLayout.setOnRefreshListener(this)
        rootView.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        adapter = MyBookmarksAdapter(activity, { v, myBookmark ->
            // onClickLister
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(myBookmark.linkUrl)))
        }, { v, myBookmark ->
            // LongClickLister
            val intent = Intent(activity, EntryInfoActivity::class.java)
            intent.putExtras(EntryInfoActivity.buildBundle(myBookmark.linkUrl))
            startActivity(intent)
        })
        rootView.listView.adapter = adapter!!
        rootView.listView.setOnScrollListener(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestApi()
    }

    override fun onDestroyView() {
        subscriptions.unsubscribe()
        super.onDestroyView()
    }

    override fun onRefresh() {
        adapter!!.clear()
        requestApi()
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (adapter!!.count == 0) return;
        val total = adapter!!.getItem(0).totalCount
        if (firstVisibleItem + visibleItemCount == totalItemCount && adapter!!.count < total && !getView().swipeLayout.isRefreshing) {
            requestApi(adapter!!.count)
        }
    }

    private fun requestApi() {
        requestApi(0)
    }

    private fun requestApi(offset: Int) {
        if (view == null) {
            return
        }
        view.swipeLayout.isRefreshing = true
        subscriptions.add(MyBookmarksApi.request(AccountDAO.get()!!, arguments.getString("keyword"), offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myBookmark ->
                    adapter!!.add(myBookmark)
                }, { e ->
                    Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show()
                    if (view != null) view.swipeLayout.isRefreshing = false
                }, {
                    if (view != null) view.swipeLayout.isRefreshing = false
                }))
    }
}