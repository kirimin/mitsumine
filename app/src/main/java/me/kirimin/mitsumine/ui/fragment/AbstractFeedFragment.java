package me.kirimin.mitsumine.ui.fragment;

import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.model.Feed;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.adapter.FeedAdapter;
import me.kirimin.mitsumine.ui.adapter.FeedAdapter.FeedAdapterListener;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

abstract public class AbstractFeedFragment extends Fragment implements FeedAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView mListView;
    private FeedAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    abstract void requestFeed();

    abstract boolean isUseReadLater();

    abstract boolean isUseRead();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.FeedFragmentSwipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mAdapter = new FeedAdapter(getActivity().getApplicationContext(), this, isUseReadLater(), isUseRead());
        mListView = (ListView) rootView.findViewById(R.id.FeedFragmentListViewFeed);
        mListView.setAdapter(mAdapter);
        requestFeed();
        return rootView;
    }

    @Override
    public void onRefresh() {
        reloadFeed();
    }

    @Override
    public void onFeedClick(View view) {
        Feed feed = (Feed) view.getTag();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feed.linkUrl)));
    }

    @Override
    public void onFeedLongClick(View view) {
        Feed feed = (Feed) view.getTag();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feed.entryLinkUrl)));
    }

    @Override
    public void onFeedLeftSlide(View view) {
        Feed feed = (Feed) view.getTag();
        feed.type = Feed.TYPE_READ;
        FeedDAO.save(feed);
        mAdapter.remove(feed);
    }

    @Override
    public void onFeedRightSlide(View view) {
        Feed feed = (Feed) view.getTag();
        feed.type = Feed.TYPE_READ_LATER;
        FeedDAO.save(feed);
        mAdapter.remove(feed);
    }

    @Override
    public void onFeedShareClick(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if (pref.getBoolean(getString(R.string.key_is_share_with_title), false)) {
            Intent intent = buildShareUrlWithTitleIntent((Feed) view.getTag());
            startActivity(Intent.createChooser(intent, getString(R.string.feed_share_url_with_title)));
        } else {
            Intent intent = buildShareUrlIntent((Feed) view.getTag());
            startActivity(Intent.createChooser(intent, getString(R.string.feed_share_url)));
        }
    }

    @Override
    public void onFeedShareLongClick(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if (!pref.getBoolean(getString(R.string.key_is_share_with_title), false)) {
            Intent intent = buildShareUrlWithTitleIntent((Feed) view.getTag());
            startActivity(Intent.createChooser(intent, getString(R.string.feed_share_url_with_title)));
        } else {
            Intent intent = buildShareUrlIntent((Feed) view.getTag());
            startActivity(Intent.createChooser(intent, getString(R.string.feed_share_url)));
        }
    }

    protected void clearFeed() {
        mAdapter.clear();
    }

    protected void setFeed(List<Feed> feedList) {
        mAdapter.addAll(feedList);
    }

    protected void showRefreshing() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    protected void dismissRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void reloadFeed() {
        if (getActivity() != null) {
            RequestQueueSingleton.getRequestQueue(getActivity()).getCache().clear();
        }
        mAdapter.clear();
        requestFeed();
    }

    private static Intent buildShareUrlIntent(Feed feed) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, feed.title);
        share.putExtra(Intent.EXTRA_TEXT, feed.linkUrl);

        return share;
    }

    private static Intent buildShareUrlWithTitleIntent(Feed feed) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, feed.title + " " + feed.linkUrl);

        return share;
    }
}
