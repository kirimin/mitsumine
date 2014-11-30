package me.kirimin.mitsumine.ui.adapter;

import me.kirimin.mitsumine.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeedPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    public interface OnSlideListener {
        public void onLeftSlide(View view);

        public void onRightSlide(View view);
    }

    private LayoutInflater mInflater;
    private View mLayout;
    private OnSlideListener mOnSlideListener;
    private boolean mUseLeft;
    private int mPageNum = 3;

    public FeedPagerAdapter(Context context, View layout, OnSlideListener listener, boolean useLeft, boolean useRight) {
        super();
        mInflater = LayoutInflater.from(context);
        mLayout = layout;
        mOnSlideListener = listener;
        mUseLeft = useLeft;
        if (!useLeft)
            mPageNum--;
        if (!useRight)
            mPageNum--;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (position == 0) {
            if (mUseLeft) {
                view = mInflater.inflate(R.layout.row_feed_read_later, null);
            } else {
                view = mLayout;
            }
        } else if (position == 1) {
            if (mUseLeft) {
                view = mLayout;
            } else {
                view = mInflater.inflate(R.layout.row_feed_read, null);
            }
        } else {
            view = mInflater.inflate(R.layout.row_feed_read, null);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return mPageNum;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset != 0) {
            return;
        }

        if (position == 0 && mUseLeft) {
            mOnSlideListener.onRightSlide(mLayout);
        } else if (position == 2 || (position == 1 && !mUseLeft)) {
            mOnSlideListener.onLeftSlide(mLayout);
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }
}