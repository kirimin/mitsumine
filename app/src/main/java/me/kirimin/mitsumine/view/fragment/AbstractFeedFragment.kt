package me.kirimin.mitsumine.view.fragment


import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.view.activity.EntryInfoActivity
import me.kirimin.mitsumine.view.adapter.FeedAdapter
import me.kirimin.mitsumine.view.adapter.FeedAdapter.FeedAdapterListener

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.fragment_feed.view.*
import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.FeedData
import me.kirimin.mitsumine.domain.FeedUseCase
import me.kirimin.mitsumine.domain.enums.Category
import me.kirimin.mitsumine.domain.enums.Type
import me.kirimin.mitsumine.presenter.FeedPresenter
import me.kirimin.mitsumine.view.FeedView

public abstract class AbstractFeedFragment : Fragment(), FeedView, FeedAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    abstract fun isUseReadLater(): Boolean
    abstract fun isUseRead(): Boolean
    abstract fun getDataInstance(): AbstractFeedData

    private var mAdapter: FeedAdapter? = null
    private val presenter: FeedPresenter = FeedPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_feed, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter.onCreate(this, FeedUseCase(getDataInstance()));
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun onFeedClick(view: View) {
        presenter.onItemClick(view.tag as Feed)
    }

    override fun onFeedLongClick(view: View) {
        presenter.onItemLongClick(view.tag as Feed)
    }

    override fun onFeedLeftSlide(view: View) {
        presenter.onFeedLeftSlide(view.tag as Feed)
    }

    override fun onFeedRightSlide(view: View) {
        presenter.onFeedRightSlide(view.tag as Feed)
    }

    override fun onFeedShareClick(view: View) {
        presenter.onFeedShareClick(view.tag as Feed)
    }

    override fun onFeedShareLongClick(view: View) {
        presenter.onFeedShareLongClick(view.tag as Feed)
    }

    override fun initViews() {
        view.swipeLayout.setColorSchemeResources(R.color.blue, R.color.orange)
        view.swipeLayout.setOnRefreshListener(this)
        view.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        mAdapter = FeedAdapter(activity.applicationContext, this, isUseReadLater(), isUseRead())
        view.feedListView.adapter = mAdapter
    }

    override fun setFeed(feedList: List<Feed>) {
        mAdapter!!.addAll(feedList)
    }

    override fun showRefreshing() {
        view.swipeLayout.isRefreshing = true
    }

    override fun dismissRefreshing() {
        view.swipeLayout.isRefreshing = false
    }

    override fun clearAllItem() {
        mAdapter!!.clear()
    }

    override fun removeItem(feed: Feed) {
        mAdapter!!.remove(feed)
    }

    override fun sendUrlIntent(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun startEntryInfoView(url: String) {
        val intent = Intent(activity, EntryInfoActivity::class.java)
        intent.putExtras(EntryInfoActivity.buildBundle(url))
        startActivity(intent)
    }

    override fun sendShareUrlIntent(title: String, url: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_SUBJECT, title)
        share.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(share)
    }

    override fun sendShareUrlWithTitleIntent(title: String, url: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_TEXT, title + " " + url)
        startActivity(share)
    }
}
