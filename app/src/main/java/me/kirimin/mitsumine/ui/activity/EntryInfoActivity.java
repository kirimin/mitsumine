package me.kirimin.mitsumine.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;
import me.kirimin.mitsumine.network.api.EntryInfoApi;
import me.kirimin.mitsumine.ui.fragment.BookmarkListFragment;
import me.kirimin.mitsumine.ui.fragment.RegisterBookmarkFragment;
import me.kirimin.mitsumine.util.EntryInfoFunc;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.adapter.EntryInfoPagerAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EntryInfoActivity extends ActionBarActivity {

    public static Bundle buildBundle(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        return bundle;
    }

    @InjectView(R.id.EntryInfoTitleTextView)
    TextView titleTextView;
    @InjectView(R.id.EntryInfoCountLayout)
    View countLayout;
    @InjectView(R.id.EntryInfoBookmarkCountTextView)
    TextView bookmarkCountTextView;
    @InjectView(R.id.EntryInfoCommentCountTextView)
    TextView commentCountTextView;
    @InjectView(R.id.EntryInfoThumbnailImageVIew)
    ImageView thumbnailImageView;
    @InjectView(R.id.EntryInfoCommentsViewPager)
    ViewPager viewPager;
    @InjectView(R.id.EntryInfoTabs)
    PagerSlidingTabStrip tabs;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_info);
        ButterKnife.inject(this);
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.entry_info_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String url = getIntent().getStringExtra("url");
        if (url == null) {
            finish();
        }
        subscriptions.add(EntryInfoApi.request(RequestQueueSingleton.get(getApplicationContext()), url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(EntryInfoFunc.mapToEntryInfo())
                .filter(EntryInfoFunc.isNotNullEntryInfo())
                .subscribe(new Action1<EntryInfo>() {
                    @Override
                    public void call(final EntryInfo entryInfo) {
                        countLayout.setVisibility(View.VISIBLE);
                        titleTextView.setText(entryInfo.getTitle());
                        bookmarkCountTextView.setText(String.valueOf(entryInfo.getBookmarkCount()));
                        Picasso.with(getApplicationContext()).load(entryInfo.getThumbnailUrl()).fit().into(thumbnailImageView);

                        final EntryInfoPagerAdapter adapter = new EntryInfoPagerAdapter(getSupportFragmentManager());
                        adapter.addPage(BookmarkListFragment.newFragment(entryInfo.getBookmarkList()), getString(R.string.entry_info_all_bookmarks));
                        subscriptions.add(Observable.from(entryInfo.getBookmarkList())
                                .filter(EntryInfoFunc.hasComment())
                                .toList()
                                .subscribe(new Action1<List<Bookmark>>() {
                                    @Override
                                    public void call(List<Bookmark> commentList) {
                                        commentCountTextView.setText(String.valueOf(commentList.size()));
                                        adapter.addPage(BookmarkListFragment.newFragment(commentList), getString(R.string.entry_info_comments));
                                        if (AccountDAO.get() != null) {
                                            adapter.addPage(RegisterBookmarkFragment.newFragment(entryInfo.getUrl()), getString(R.string.entry_info_register_bookmark));
                                        }
                                        viewPager.setAdapter(adapter);
                                        viewPager.setCurrentItem(1);
                                        viewPager.setOffscreenPageLimit(2);
                                        tabs.setViewPager(viewPager);
                                    }
                                }));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        subscriptions.unsubscribe();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
