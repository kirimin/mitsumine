package me.kirimin.mitsumine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.model.MyBookmark;
import me.kirimin.mitsumine.network.api.MyBookmarksApi;
import me.kirimin.mitsumine.ui.adapter.MyBookmarksAdapter;
import me.kirimin.mitsumine.util.MyBookmarksFunc;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MyBookmarksActivity extends ActionBarActivity implements MyBookmarksAdapter.OnMyBookmarkClickListener {

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookmarks);
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Account account = AccountDAO.get();
        if (account == null) {
            finish();
            return;
        }
        actionBar.setTitle(getString(R.string.my_bookmarks_title));
        final ListView listView = (ListView) findViewById(R.id.MyBookmarksListView);
        final MyBookmarksAdapter adapter = new MyBookmarksAdapter(this, this);
        subscriptions.add(MyBookmarksApi.request(account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(MyBookmarksFunc.mapToMyBookmarkList())
                .subscribe(new Observer<MyBookmark>() {
                    @Override
                    public void onCompleted() {
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MyBookmark myBookmark) {
                        adapter.add(myBookmark);
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

    @Override
    public void onMyBookmarkClick(View v, MyBookmark myBookmark) {
        Intent intent = new Intent(MyBookmarksActivity.this, EntryInfoActivity.class);
        intent.putExtras(EntryInfoActivity.buildBundle(myBookmark.getLinkUrl()));
        startActivity(intent);
    }
}