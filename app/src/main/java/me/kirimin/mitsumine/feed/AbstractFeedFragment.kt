package me.kirimin.mitsumine.feed

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.entryinfo.EntryInfoActivity

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

import kotlinx.android.synthetic.main.fragment_feed.view.*
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine._common.ui.BaseFragment
import rx.Subscription
import javax.inject.Inject

/**
 * フィードを表示する画面で共通して使用する親Fragment
 */
abstract class AbstractFeedFragment : BaseFragment(), FeedView, View.OnClickListener, View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: FeedAdapter
    @Inject
    lateinit var presenter: FeedPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun onClick(v: View) {
        val feed = v.tag as Feed
        when (v.id) {
            R.id.card_view -> {
                presenter.onItemClick(feed)
            }
            R.id.FeedFragmentImageViewShare -> {
                presenter.onFeedShareClick(feed)
            }
        }
    }

    override fun onLongClick(v: View): Boolean {
        val feed = v.tag as Feed
        when (v.id) {
            R.id.card_view -> {
                presenter.onItemLongClick(feed)
            }
            R.id.FeedFragmentImageViewShare -> {
                presenter.onFeedShareLongClick(feed)
                return true
            }
        }
        return false
    }

    override fun initViews(isUseRead: Boolean, isUseReadLater: Boolean) {
        val view = view ?: return
        view.swipeLayout.setColorSchemeResources(R.color.blue, R.color.orange)
        view.swipeLayout.setOnRefreshListener(this)
        view.swipeLayout.setProgressViewOffset(false, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt())
        adapter = FeedAdapter(context = activity.applicationContext, presenter = presenter, useReadLater = isUseReadLater, useRead = isUseRead)
        view.feedListView.adapter = adapter
    }

    override fun setFeed(feedList: List<Feed>) {
        adapter.addAll(feedList)
    }

    override fun showRefreshing() {
        view?.swipeLayout?.isRefreshing = true
    }

    override fun dismissRefreshing() {
        view?.swipeLayout?.isRefreshing = false
    }

    override fun clearAllItem() {
        adapter.clear()
    }

    override fun removeItem(feed: Feed) {
        adapter.remove(feed)
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
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_SUBJECT, title)
        share.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(share)
    }

    override fun sendShareUrlWithTitleIntent(title: String, url: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_TEXT, title + " " + url)
        startActivity(share)
    }

    override fun initListViewCell(holder: FeedAdapter.ViewHolder, feed: Feed) {
        holder.root.tag = feed
        holder.cassette.tag = feed
        holder.cassette.setOnClickListener(this)
        holder.cassette.setOnLongClickListener(this)
        holder.share.tag = feed
        holder.share.setOnClickListener(this)
        holder.share.setOnLongClickListener(this)
        holder.title.text = feed.title
        holder.content.text = feed.content
        holder.domain.text = feed.linkUrl
        holder.thumbnail.setImageResource(R.drawable.no_image)
        holder.tags.visibility = View.INVISIBLE;
        holder.bookmarkCount.visibility = View.GONE
        if (holder.bookmarkCount.tag is Subscription) {
            (holder.bookmarkCount.tag as Subscription).unsubscribe()
        }
        if (holder.tags.tag is Subscription) {
            (holder.tags.tag as Subscription).unsubscribe()
        }
    }

    override fun setListViewCellPagerPosition(holder: FeedAdapter.ViewHolder, position: Int) {
        holder.viewPager.currentItem = position
    }

    override fun setTagList(holder: FeedAdapter.ViewHolder, tags: String) {
        holder.tags.text = tags
        holder.tags.visibility = View.VISIBLE
    }

    override fun setBookmarkCount(holder: FeedAdapter.ViewHolder, count: String) {
        holder.bookmarkCount.text = count
        holder.bookmarkCount.visibility = View.VISIBLE
    }

    override fun loadThumbnailImage(holder: FeedAdapter.ViewHolder, url: String) {
        Picasso.with(context).load(url).into(holder.thumbnail)
    }

    override fun loadFaviconImage(holder: FeedAdapter.ViewHolder, url: String) {
        Picasso.with(context).load(url).into(holder.favicon)
    }

    override fun injection() {
        (activity.application as MyApplication).getApplicationComponent().inject(this)
    }
}
