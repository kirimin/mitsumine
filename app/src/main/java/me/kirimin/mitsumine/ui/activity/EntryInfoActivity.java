package me.kirimin.mitsumine.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;
import me.kirimin.mitsumine.network.EntryInfoAccessor;
import me.kirimin.mitsumine.network.RequestQueueSingleton;
import me.kirimin.mitsumine.ui.activity.search.UserSearchActivity;
import me.kirimin.mitsumine.ui.adapter.EntryInfoAdapter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class EntryInfoActivity extends ActionBarActivity implements EntryInfoAdapter.EntryInfoAdapterListener {

    public static Intent buildStartActivityIntent(Context context, String url) {
        Intent intent = new Intent(context, EntryInfoActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    private EntryInfoAdapter adapter;

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
        adapter = new EntryInfoAdapter(this, this);
        ListView listView = (ListView) findViewById(R.id.EntryInfoListView);
        listView.setAdapter(adapter);
        EntryInfoAccessor.request(RequestQueueSingleton.getRequestQueue(getApplicationContext()), new EntryInfoAccessor.EntryInfoListener() {
            @Override
            public void onSuccess(final EntryInfo feedDetail) {
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
                            public void call(List<Bookmark> b) {
                                adapter.addAll(b);
                                TextView commentCountText = (TextView) findViewById(R.id.EntryInfoCommentCountTextView);
                                commentCountText.setText(String.valueOf(b.size()));
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

    @Override
    public void onCommentClick(View v, Bookmark bookmark) {
        Intent intent = new Intent(this, UserSearchActivity.class);
        intent.putExtras(UserSearchActivity.buildBundle(bookmark.getUser()));
        startActivity(intent);
    }
}
