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

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;
import me.kirimin.mitsumine.network.api.EntryInfoApiAccessor;
import me.kirimin.mitsumine.ui.fragment.EntryInfoFragment;
import me.kirimin.mitsumine.ui.fragment.RegisterBookmarkFragment;
import me.kirimin.mitsumine.util.EntryInfoFunc;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.adapter.EntryInfoPagerAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EntryInfoActivity extends ActionBarActivity {

    public static Bundle buildBundle(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_info);
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.entry_info_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String url = getIntent().getStringExtra("url");
        if (url == null) {
            finish();
        }
        EntryInfoApiAccessor.request(RequestQueueSingleton.getRequestQueue(getApplicationContext()), url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(EntryInfoFunc.mapToEntryInfo())
                .filter(EntryInfoFunc.isNotNullEntryInfo())
                .subscribe(new Action1<EntryInfo>() {
                    @Override
                    public void call(final EntryInfo entryInfo) {
                        findViewById(R.id.EntryInfoCountLayout).setVisibility(View.VISIBLE);
                        TextView titleText = (TextView) findViewById(R.id.EntryInfoTitleTextView);
                        titleText.setText(entryInfo.getTitle());
                        TextView bookmarkCountText = (TextView) findViewById(R.id.EntryInfoBookmarkCountTextView);
                        bookmarkCountText.setText(String.valueOf(entryInfo.getBookmarkCount()));
                        ImageView thumbnail = (ImageView) findViewById(R.id.EntryInfoThumbnailImageVIew);
                        Picasso.with(getApplicationContext()).load(entryInfo.getThumbnailUrl()).fit().into(thumbnail);

                        final EntryInfoPagerAdapter adapter = new EntryInfoPagerAdapter(getSupportFragmentManager());
                        adapter.addPage(EntryInfoFragment.newFragment(entryInfo.getBookmarkList()), getString(R.string.entry_info_all_bookmarks));
                        Observable.from(entryInfo.getBookmarkList())
                                .filter(EntryInfoFunc.hasComment())
                                .toList()
                                .subscribe(new Action1<List<Bookmark>>() {
                                    @Override
                                    public void call(List<Bookmark> commentList) {
                                        TextView commentCountText = (TextView) findViewById(R.id.EntryInfoCommentCountTextView);
                                        commentCountText.setText(String.valueOf(commentList.size()));

                                        adapter.addPage(EntryInfoFragment.newFragment(commentList), getString(R.string.entry_info_comments));
                                        if (AccountDAO.get() != null) {
                                            adapter.addPage(RegisterBookmarkFragment.newFragment(entryInfo.getUrl()), getString(R.string.entry_info_register_bookmark));
                                        }
                                        ViewPager viewPager = (ViewPager) findViewById(R.id.EntryInfoCommentsViewPager);
                                        viewPager.setAdapter(adapter);
                                        viewPager.setCurrentItem(1);
                                        viewPager.setOffscreenPageLimit(2);
                                        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.EntryInfoTabs);
                                        tabs.setViewPager(viewPager);
                                    }
                                });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
