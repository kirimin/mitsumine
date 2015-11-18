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

public class FeedAdapter(context: Context, private val presenter: FeedPresenter, val mUseReadLater: Boolean, val mUseRead: Boolean) : ArrayAdapter<Feed>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_pager, null)
            val viewPager = view.findViewById(R.id.FeedFragmentViewPager) as ViewPager
            val feedView = LayoutInflater.from(context).inflate(R.layout.row_feed, null)
            val adapter = FeedPagerAdapter(context, feedView, object : OnSlideListener {

                override fun onLeftSlide(view: View) {
                    if (mUseReadLater) {
                        viewPager.currentItem = 1
                    } else {
                        viewPager.currentItem = 0
                    }
                    //mListener.onFeedLeftSlide(view)
                }

                override fun onRightSlide(view: View) {
                    viewPager.currentItem = 1
                    //mListener.onFeedRightSlide(view)
                }
            }, mUseReadLater, mUseRead)
            viewPager.adapter = adapter
            viewPager.setOnPageChangeListener(adapter)
            if (mUseReadLater) {
                viewPager.setCurrentItem(1, false)
            } else {
                viewPager.setCurrentItem(0, false)
            }
            val holder = ViewHolder(feedView)
            view.tag = holder
        } else {
            view = convertView
        }
        presenter.onGetView(view.tag as ViewHolder, getItem(position));
        return view
    }

    class ViewHolder(view: View){
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
