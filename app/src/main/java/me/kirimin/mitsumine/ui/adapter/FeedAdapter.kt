package me.kirimin.mitsumine.ui.adapter

import com.squareup.picasso.Picasso

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.network.RequestQueueSingleton
import me.kirimin.mitsumine.network.api.BookmarkCountApi
import me.kirimin.mitsumine.ui.adapter.FeedPagerAdapter.OnSlideListener
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.android.view.ViewObservable
import rx.functions.Action1
import rx.schedulers.Schedulers

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import me.kirimin.mitsumine.network.api.TagListApi

public class FeedAdapter(context: Context, private val mListener: FeedAdapter.FeedAdapterListener, private val mUseReadLater: Boolean, private val mUseRead: Boolean) : ArrayAdapter<Feed>(context, 0), OnClickListener, OnLongClickListener {

    public interface FeedAdapterListener {
        public fun onFeedLeftSlide(view: View)

        public fun onFeedRightSlide(view: View)

        public fun onFeedClick(view: View)

        public fun onFeedLongClick(view: View)

        public fun onFeedShareClick(view: View)

        public fun onFeedShareLongClick(view: View)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_pager, null)
            val viewPager = view.findViewById(R.id.FeedFragmentViewPager) as ViewPager
            val feedView = LayoutInflater.from(getContext()).inflate(R.layout.row_feed, null)
            val adapter = FeedPagerAdapter(getContext(), feedView, object : OnSlideListener {

                override fun onLeftSlide(view: View) {
                    if (mUseReadLater) {
                        viewPager.setCurrentItem(1)
                    } else {
                        viewPager.setCurrentItem(0)
                    }
                    mListener.onFeedLeftSlide(view)
                }

                override fun onRightSlide(view: View) {
                    viewPager.setCurrentItem(1)
                    mListener.onFeedRightSlide(view)
                }
            }, mUseReadLater, mUseRead)
            viewPager.setAdapter(adapter)
            viewPager.setOnPageChangeListener(adapter)
            if (mUseReadLater) {
                viewPager.setCurrentItem(1, false)
            } else {
                viewPager.setCurrentItem(0, false)
            }

            val holder = ViewHolder(feedView,
                    feedView.findViewById(R.id.FeedFragmentImageViewThumbnail) as ImageView,
                    feedView.findViewById(R.id.FeedFragmentImageViewFavicon) as ImageView,
                    feedView.findViewById(R.id.FeedFragmentImageViewShare) as ImageView,
                    feedView.findViewById(R.id.FeedFragmentTextViewTitle) as TextView,
                    feedView.findViewById(R.id.FeedFragmentTextViewContent) as TextView,
                    feedView.findViewById(R.id.FeedFragmentTextViewDomain) as TextView,
                    feedView.findViewById(R.id.FeedFragmentImageViewBookmarkCount) as TextView,
                    feedView.findViewById(R.id.FeedFragmentTextViewTags) as TextView)
            view.setTag(holder)
        } else {
            view = convertView
        }

        val holder = view.getTag() as ViewHolder
        val feed = getItem(position)
        holder.mFeedView.setTag(feed)
        holder.mFeedView.setOnClickListener(this)
        holder.mFeedView.setOnLongClickListener(this)
        holder.mShare.setTag(feed)
        holder.mShare.setOnClickListener(this)
        holder.mShare.setOnLongClickListener(this)
        holder.mTitle.setText(feed.title)
        holder.mContent.setText(feed.content)
        holder.mDomain.setText(feed.linkUrl)

        try {
            (holder.mBookmarkCount.getTag() as Subscription).unsubscribe()
            holder.mBookmarkCount.setVisibility(View.GONE)
            (holder.mTags.getTag() as Subscription).unsubscribe()
            holder.mTags.setVisibility(View.INVISIBLE);
        } catch (e: TypeCastException) {
        }
        val subscription = ViewObservable.bindView<String>(holder.mBookmarkCount,
                BookmarkCountApi.request(RequestQueueSingleton.get(getContext()), feed.linkUrl))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    holder.mBookmarkCount.setText(s)
                    holder.mBookmarkCount.setVisibility(View.VISIBLE)
                }, { e ->
                    holder.mBookmarkCount.setVisibility(View.GONE)
                })
        holder.mBookmarkCount.setTag(subscription)
        holder.mThumbnail.setImageResource(R.drawable.no_image)

        val subscription2 = ViewObservable.bindView<List<String>>(holder.mTags,
                TagListApi.request(RequestQueueSingleton.get(getContext()), feed.linkUrl))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tags ->
                    holder.mTags.setText(tags.joinToString(", "))
                    holder.mTags.setVisibility(View.VISIBLE)
                }, { e ->
                    holder.mTags.setVisibility(View.INVISIBLE)
                })
        holder.mTags.setTag(subscription2)

        if (!feed.thumbnailUrl.isEmpty()) {
            Picasso.with(getContext()).load(feed.thumbnailUrl).into(holder.mThumbnail)
        }
        if (!feed.faviconUrl.isEmpty()) {
            Picasso.with(getContext()).load(feed.faviconUrl).into(holder.mFavicon)
        }
        return view
    }

    override fun onClick(v: View) {
        if (v.getId() == R.id.FeedFragmentImageViewShare) {
            mListener.onFeedShareClick(v)
        } else {
            mListener.onFeedClick(v)
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (v.getId() == R.id.FeedFragmentImageViewShare) {
            mListener.onFeedShareLongClick(v)
        } else {
            mListener.onFeedLongClick(v)
        }
        return true
    }

    private class ViewHolder(
            val mFeedView: View,
            val mThumbnail: ImageView,
            val mFavicon: ImageView,
            val mShare: ImageView,
            val mTitle: TextView,
            val mContent: TextView,
            val mDomain: TextView,
            val mBookmarkCount: TextView,
            val mTags: TextView) {}
}