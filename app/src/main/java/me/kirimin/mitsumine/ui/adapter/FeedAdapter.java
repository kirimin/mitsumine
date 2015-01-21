package me.kirimin.mitsumine.ui.adapter;

import com.squareup.picasso.Picasso;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.network.api.BookmarkCountApi;
import me.kirimin.mitsumine.ui.adapter.FeedPagerAdapter.OnSlideListener;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.ViewObservable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAdapter extends ArrayAdapter<Feed> implements OnClickListener, OnLongClickListener {

    private FeedAdapterListener mListener;
    private boolean mUseReadLater, mUseRead;

    public interface FeedAdapterListener {
        public void onFeedLeftSlide(View view);

        public void onFeedRightSlide(View view);

        public void onFeedClick(View view);

        public void onFeedLongClick(View view);

        public void onFeedShareClick(View view);

        public void onFeedShareLongClick(View view);
    }

    public FeedAdapter(Context context, FeedAdapterListener listener, boolean useReadLater, boolean useRead) {
        super(context, 0);
        mListener = listener;
        mUseReadLater = useReadLater;
        mUseRead = useRead;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_pager, null);
            final ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.FeedFragmentViewPager);
            View feedView = LayoutInflater.from(getContext()).inflate(R.layout.row_feed, null);
            FeedPagerAdapter adapter = new FeedPagerAdapter(getContext(), feedView, new OnSlideListener() {

                @Override
                public void onLeftSlide(View view) {
                    if (mUseReadLater) {
                        viewPager.setCurrentItem(1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                    mListener.onFeedLeftSlide(view);
                }

                @Override
                public void onRightSlide(View view) {
                    viewPager.setCurrentItem(1);
                    mListener.onFeedRightSlide(view);
                }
            }, mUseReadLater, mUseRead);
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(adapter);
            if (mUseReadLater) {
                viewPager.setCurrentItem(1, false);
            } else {
                viewPager.setCurrentItem(0, false);
            }

            ViewHolder holder = new ViewHolder();
            holder.mFeedView = feedView;
            holder.mThumbnail = (ImageView) feedView.findViewById(R.id.FeedFragmentImageViewThumbnail);
            holder.mFavicon = (ImageView) feedView.findViewById(R.id.FeedFragmentImageViewFavicon);
            holder.mShare = (ImageView) feedView.findViewById(R.id.FeedFragmentImageViewShare);
            holder.mTitle = (TextView) feedView.findViewById(R.id.FeedFragmentTextViewTitle);
            holder.mContent = (TextView) feedView.findViewById(R.id.FeedFragmentTextViewContent);
            holder.mDomain = (TextView) feedView.findViewById(R.id.FeedFragmentTextViewDomain);
            holder.mBookmarkCount = (TextView) feedView.findViewById(R.id.FeedFragmentImageViewBookmarkCount);
            convertView.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final Feed feed = getItem(position);
        holder.mFeedView.setTag(feed);
        holder.mFeedView.setOnClickListener(this);
        holder.mFeedView.setOnLongClickListener(this);
        holder.mShare.setTag(feed);
        holder.mShare.setOnClickListener(this);
        holder.mShare.setOnLongClickListener(this);
        holder.mTitle.setText(feed.title);
        holder.mContent.setText(feed.content);
        holder.mDomain.setText(feed.linkUrl);
        Subscription oldSubscription = (Subscription) holder.mDomain.getTag();
        if (oldSubscription != null) {
            oldSubscription.unsubscribe();
            holder.mBookmarkCount.setText("");
        }
        Subscription subscription = ViewObservable.bindView(holder.mBookmarkCount, BookmarkCountApi.request(RequestQueueSingleton.getRequestQueue(getContext()), feed.linkUrl))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        holder.mBookmarkCount.setText(s);
                    }
                });
        holder.mBookmarkCount.setTag(subscription);
        holder.mThumbnail.setImageResource(R.drawable.no_image);

        if (!feed.thumbnailUrl.isEmpty()) {
            Picasso.with(getContext()).load(feed.thumbnailUrl).into(holder.mThumbnail);
        }
        if (!feed.faviconUrl.isEmpty()) {
            Picasso.with(getContext()).load(feed.faviconUrl).into(holder.mFavicon);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.FeedFragmentImageViewShare) {
            mListener.onFeedShareClick(v);
        } else {
            mListener.onFeedClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.FeedFragmentImageViewShare) {
            mListener.onFeedShareLongClick(v);
        } else {
            mListener.onFeedLongClick(v);
        }
        return false;
    }

    private static class ViewHolder {
        View mFeedView;
        ImageView mThumbnail;
        ImageView mFavicon;
        ImageView mShare;
        TextView mTitle;
        TextView mContent;
        TextView mDomain;
        TextView mBookmarkCount;
    }
}