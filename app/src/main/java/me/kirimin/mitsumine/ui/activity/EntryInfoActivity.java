package me.kirimin.mitsumine.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;
import me.kirimin.mitsumine.network.EntryInfoAccessor;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.activity.search.UserSearchActivity;
import me.kirimin.mitsumine.ui.adapter.EntryInfoAdapter;
import me.kirimin.mitsumine.ui.adapter.EntryInfoPagerAdapter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class EntryInfoActivity extends ActionBarActivity {

    public static Bundle buildBundle(Context context, String url) {
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

        EntryInfoAccessor.request(RequestQueueSingleton.getRequestQueue(getApplicationContext()), new EntryInfoAccessor.EntryInfoListener() {
            @Override
            public void onSuccess(final EntryInfo feedDetail) {
                findViewById(R.id.EntryInfoCountLayout).setVisibility(View.VISIBLE);
                TextView titleText = (TextView) findViewById(R.id.EntryInfoTitleTextView);
                titleText.setText(feedDetail.getTitle());
                TextView bookmarkCountText = (TextView) findViewById(R.id.EntryInfoBookmarkCountTextView);
                bookmarkCountText.setText(String.valueOf(feedDetail.getBookmarkCount()));
                ImageView thumbnail = (ImageView) findViewById(R.id.EntryInfoThumbnailImageVIew);
                Picasso.with(getApplicationContext()).load(feedDetail.getThumbnailUrl()).fit().into(thumbnail);

                Observable.from(feedDetail.getBookmarkList())
                        .filter(new Func1<Bookmark, Boolean>() {
                            @Override
                            public Boolean call(Bookmark bookmark) {
                                return !bookmark.getComment().equals("");
                            }
                        })
                        .toList()
                        .subscribe(new Action1<List<Bookmark>>() {
                            @Override
                            public void call(List<Bookmark> commentList) {
                                TextView commentCountText = (TextView) findViewById(R.id.EntryInfoCommentCountTextView);
                                commentCountText.setText(String.valueOf(commentList.size()));
                                ViewPager viewPager = (ViewPager) findViewById(R.id.EntryInfoCommentsViewPager);
                                viewPager.setAdapter(new EntryInfoPagerAdapter(getSupportFragmentManager(), feedDetail.getBookmarkList(), commentList, getApplicationContext()));
                                viewPager.setCurrentItem(1);
                                PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.EntryInfoTabs);
                                tabs.setViewPager(viewPager);
                            }
                        });
            }

            @Override
            public void onError(String errorMessage) {
            }
        }, url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
