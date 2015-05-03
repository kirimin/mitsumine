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
import android.widget.ListView
import android.widget.Toast

import org.json.JSONException
import org.json.JSONObject

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.model.MyBookmark
import me.kirimin.mitsumine.network.api.MyBookmarksApi
import me.kirimin.mitsumine.ui.activity.EntryInfoActivity
import me.kirimin.mitsumine.ui.adapter.MyBookmarksAdapter
import me.kirimin.mitsumine.util.MyBookmarksFunc
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.fragment_my_bookmarks.view.*

public class MyBookmarksFragment : Fragment(), MyBookmarksAdapter.OnMyBookmarkClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    companion object {

        public fun newFragment(keyword: String): MyBookmarksFragment {
            val fragment = MyBookmarksFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    private val subscriptions = CompositeSubscription()
    private var total = 0
    private var adapter: MyBookmarksAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_my_bookmarks, container, false)
        rootView.swipeLayout.setColorSchemeResources(R.color.blue, R.color.orange)
        rootView.swipeLayout.setOnRefreshListener(this)
        rootView.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, getResources().getDisplayMetrics()).toInt())
        adapter = MyBookmarksAdapter(getActivity(), this)
        rootView.listView.setAdapter(adapter!!)
        rootView.listView.setOnScrollListener(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)
        requestApi()
    }

    override fun onDestroyView() {
        subscriptions.unsubscribe()
        super<Fragment>.onDestroyView()
    }

    override fun onMyBookmarkClick(v: View, myBookmark: MyBookmark) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(myBookmark.getLinkUrl())))
    }

    override fun onMyBookmarkLongClick(v: View, myBookmark: MyBookmark) {
        val intent = Intent(getActivity(), javaClass<EntryInfoActivity>())
        intent.putExtras(EntryInfoActivity.buildBundle(myBookmark.getLinkUrl()))
        startActivity(intent)
    }

    override fun onRefresh() {
        adapter!!.clear()
        requestApi()
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && adapter!!.getCount() < total && !getView().swipeLayout.isRefreshing()) {
            requestApi(adapter!!.getCount())
        }
    }

    private fun requestApi() {
        requestApi(0)
    }

    private fun requestApi(offset: Int) {
        if (getView() == null) {
            return
        }
        val account = AccountDAO.get()
        getView().swipeLayout.setRefreshing(true)
        subscriptions.add(MyBookmarksApi.request(account, getArguments().getString("keyword"), offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { jsonObject ->
                    total = jsonObject.getJSONObject("meta").getInt("total")
                    jsonObject
                }
                .flatMap(MyBookmarksFunc.mapToMyBookmarkList())
                .subscribe({ myBookmark ->
                    adapter!!.add(myBookmark)
                }, { e ->
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show()
                    if (getView() != null) getView().swipeLayout.setRefreshing(false)
                }, {
                    if (getView() != null) getView().swipeLayout.setRefreshing(false)
                }))
    }
}