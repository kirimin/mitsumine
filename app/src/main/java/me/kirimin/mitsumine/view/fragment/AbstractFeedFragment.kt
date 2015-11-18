package me.kirimin.mitsumine.view.fragment


import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.view.activity.EntryInfoActivity
import me.kirimin.mitsumine.view.adapter.FeedAdapter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.fragment_feed.view.*
import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.network.api.BookmarkCountApi
import me.kirimin.mitsumine.data.network.api.TagListApi
import me.kirimin.mitsumine.domain.FeedUseCase
import me.kirimin.mitsumine.presenter.FeedPresenter
import me.kirimin.mitsumine.view.FeedView
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.android.view.ViewObservable
import rx.schedulers.Schedulers

public abstract class AbstractFeedFragment : Fragment(), FeedView, View.OnClickListener, View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    abstract fun isUseReadLater(): Boolean
    abstract fun isUseRead(): Boolean
    abstract fun getDataInstance(): AbstractFeedData

    private var adapter: FeedAdapter? = null
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

    override fun onClick(v: View) {
        presenter.onClick(v.id, v.tag as Feed)
    }

    override fun onLongClick(v: View): Boolean {
        return presenter.onLongClick(v.id, v.tag as Feed)
    }

    //
    //    override fun onFeedLeftSlide(view: View) {
    //        presenter.onFeedLeftSlide(view.tag as Feed)
    //    }
    //
    //    override fun onFeedRightSlide(view: View) {
    //        presenter.onFeedRightSlide(view.tag as Feed)
    //    }

    override fun initViews() {
        view.swipeLayout.setColorSchemeResources(R.color.blue, R.color.orange)
        view.swipeLayout.setOnRefreshListener(this)
        view.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        adapter = FeedAdapter(activity.applicationContext, presenter, isUseReadLater(), isUseRead())
        view.feedListView.adapter = adapter
    }

    override fun setFeed(feedList: List<Feed>) {
        adapter!!.addAll(feedList)
    }

    override fun showRefreshing() {
        view.swipeLayout.isRefreshing = true
    }

    override fun dismissRefreshing() {
        view.swipeLayout.isRefreshing = false
    }

    override fun clearAllItem() {
        adapter!!.clear()
    }

    override fun removeItem(feed: Feed) {
        adapter!!.remove(feed)
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

    override fun initListViewCell(holder: FeedAdapter.ViewHolder, feed: Feed) {
        holder.cassette.tag = feed
        holder.cassette.setOnClickListener(this)
        holder.cassette.setOnLongClickListener(this)
        holder.share.tag = feed
        holder.share.setOnClickListener(this)
        holder.share.setOnLongClickListener(this)
        holder.title.text = feed.title
        holder.content.text = feed.content
        holder.domain.text = feed.linkUrl

        try {
            (holder.bookmarkCount.tag as Subscription).unsubscribe()
            holder.bookmarkCount.visibility = View.GONE
            (holder.tags.tag as Subscription).unsubscribe()
            holder.tags.visibility = View.INVISIBLE;
        } catch (e: TypeCastException) {
        }
        val subscription = ViewObservable.bindView<String>(holder.bookmarkCount,
                BookmarkCountApi.request(context.applicationContext, feed.linkUrl))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    holder.bookmarkCount.text = s
                    holder.bookmarkCount.visibility = View.VISIBLE
                }, { e ->
                    holder.bookmarkCount.visibility = View.GONE
                })
        holder.bookmarkCount.tag = subscription
        holder.thumbnail.setImageResource(R.drawable.no_image)

        val subscription2 = ViewObservable.bindView<List<String>>(holder.tags,
                TagListApi.request(context.applicationContext, feed.linkUrl))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tags ->
                    holder.tags.text = tags.joinToString(", ")
                    holder.tags.visibility = View.VISIBLE
                }, { e ->
                    holder.tags.visibility = View.INVISIBLE
                })
        holder.tags.tag = subscription2

        if (!feed.thumbnailUrl.isEmpty()) {
            Picasso.with(context).load(feed.thumbnailUrl).into(holder.thumbnail)
        }
        if (!feed.faviconUrl.isEmpty()) {
            Picasso.with(context).load(feed.faviconUrl).into(holder.favicon)
        }
    }
}
