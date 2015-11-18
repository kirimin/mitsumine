package me.kirimin.mitsumine.view.adapter

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.domain.model.Feed
import me.kirimin.mitsumine.view.adapter.FeedPagerAdapter.OnSlideListener

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import me.kirimin.mitsumine.presenter.FeedPresenter

public class FeedAdapter(context: Context, private val presenter: FeedPresenter, val useReadLater: Boolean, val useRead: Boolean) : ArrayAdapter<Feed>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_pager, null)
            val viewPager = view.findViewById(R.id.FeedFragmentViewPager) as ViewPager
            val feedView = LayoutInflater.from(context).inflate(R.layout.row_feed, null)
            val holder = ViewHolder(feedView, viewPager)
            val adapter = FeedPagerAdapter(context, feedView, object : OnSlideListener {
                override fun onLeftSlide(view: View) {
                    presenter.onFeedLeftSlide(holder, view.tag as Feed, useReadLater);
                }

                override fun onRightSlide(view: View) {
                    presenter.onFeedRightSlide(holder, view.tag as Feed)
                }
            }, useReadLater, useRead)
            viewPager.adapter = adapter
            viewPager.addOnPageChangeListener(adapter)
            viewPager.currentItem = if (useReadLater) 1 else 0
            view.tag = holder
        } else {
            view = convertView
        }
        presenter.onGetView(view.tag as ViewHolder, getItem(position));
        return view
    }

    class ViewHolder(view: View, val viewPager: ViewPager) {
        val cassette: View = view.findViewById(R.id.card_view)
        val thumbnail: ImageView = view.findViewById(R.id.FeedFragmentImageViewThumbnail) as ImageView
        val favicon: ImageView = view.findViewById(R.id.FeedFragmentImageViewFavicon) as ImageView
        val share: ImageView = view.findViewById(R.id.FeedFragmentImageViewShare) as ImageView
        val title: TextView = view.findViewById(R.id.FeedFragmentTextViewTitle) as TextView
        val content: TextView = view.findViewById(R.id.FeedFragmentTextViewContent) as TextView
        val domain: TextView = view.findViewById(R.id.FeedFragmentTextViewDomain) as TextView
        val bookmarkCount: TextView = view.findViewById(R.id.FeedFragmentImageViewBookmarkCount) as TextView
        val tags: TextView = view.findViewById(R.id.FeedFragmentTextViewTags) as TextView
    }
}
