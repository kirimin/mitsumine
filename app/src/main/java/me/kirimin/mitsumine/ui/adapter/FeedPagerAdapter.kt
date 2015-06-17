package me.kirimin.mitsumine.ui.adapter

import me.kirimin.mitsumine.R

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

public class FeedPagerAdapter(context: Context, private val mLayout: View, private val mOnSlideListener: FeedPagerAdapter.OnSlideListener, private val mUseLeft: Boolean, useRight: Boolean) : PagerAdapter(), ViewPager.OnPageChangeListener {

    public interface OnSlideListener {
        public fun onLeftSlide(view: View)

        public fun onRightSlide(view: View)
    }

    private val mInflater: LayoutInflater
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mPageNum = 3

    init {
        mInflater = LayoutInflater.from(context)
        if (!mUseLeft)
            mPageNum--
        if (!useRight)
            mPageNum--
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        if (position == 0) {
            if (mUseLeft) {
                view = mInflater.inflate(R.layout.row_feed_read_later, null)
            } else {
                view = mLayout
            }
        } else if (position == 1) {
            if (mUseLeft) {
                view = mLayout
            } else {
                view = mInflater.inflate(R.layout.row_feed_read, null)
            }
        } else {
            view = mInflater.inflate(R.layout.row_feed_read, null)
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mPageNum
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun onPageSelected(position: Int) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout!!.setEnabled(true)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mSwipeRefreshLayout == null) {
            mSwipeRefreshLayout = searchSwipeRefreshLayout(mLayout)
        }
        // スクロール干渉防止
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout!!.setEnabled(positionOffset == 0f)
        }
        if (positionOffset != 0f) {
            return
        }

        if (position == 0 && mUseLeft) {
            mOnSlideListener.onRightSlide(mLayout)
        } else if (position == 2 || (position == 1 && !mUseLeft)) {
            mOnSlideListener.onLeftSlide(mLayout)
        }
    }

    override fun onPageScrollStateChanged(position: Int) {
    }

    private fun searchSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        if (view.getParent() == null) {
            return null
        }
        if (view.getParent() is SwipeRefreshLayout) {
            return view.getParent() as SwipeRefreshLayout
        } else {
            return searchSwipeRefreshLayout(view.getParent() as View)
        }
    }
}