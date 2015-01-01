package me.kirimin.mitsumine.ui.activity;

import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.db.KeywordDAO;
import me.kirimin.mitsumine.db.UserIdDAO;
import me.kirimin.mitsumine.network.api.FeedApiAccessor.CATEGORY;
import me.kirimin.mitsumine.network.api.FeedApiAccessor.TYPE;
import me.kirimin.mitsumine.ui.activity.search.KeywordSearchActivity;
import me.kirimin.mitsumine.ui.activity.search.UserSearchActivity;
import me.kirimin.mitsumine.ui.fragment.FeedFragment;

import me.kirimin.mitsumine.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CATEGORY mSelectedCategory;
    private TYPE mSelectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        // 古い既読を削除
        FeedDAO.deleteOldData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.feed_main);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String[] data = new String[] { getString(R.string.feed_hot), getString(R.string.feed_new) };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, data);
        actionBar.setListNavigationCallbacks(adapter, this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        toolbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        mSelectedCategory = CATEGORY.MAIN;
        TextView textButtonRead = (TextView) findViewById(R.id.MainNavigationReadTextView);
        textButtonRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopActivity.this, ReadActivity.class));
                mDrawerLayout.closeDrawers();
            }
        });
        TextView textButtonSettings = (TextView) findViewById(R.id.MainNavigationSettingsTextView);
        textButtonSettings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopActivity.this, SettingsActivity.class));
                mDrawerLayout.closeDrawers();
            }
        });
        TextView textButtonKeywordSearch = (TextView) findViewById(R.id.MainNavigationKeywordSearchTextView);
        textButtonKeywordSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopActivity.this, KeywordSearchActivity.class));
                mDrawerLayout.closeDrawers();
            }
        });
        TextView textButtonUserSearch = (TextView) findViewById(R.id.MainNavigationUserSearchTextView);
        textButtonUserSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopActivity.this, UserSearchActivity.class));
                mDrawerLayout.closeDrawers();
            }
        });

        final LinearLayout navigationLayout = (LinearLayout) findViewById(R.id.MainNavigationCategories);
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_main), CATEGORY.MAIN));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_social), CATEGORY.SOCIAL));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_economics), CATEGORY.ECONOMICS));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_life), CATEGORY.LIFE));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_knowledge), CATEGORY.KNOWLEDGE));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_it), CATEGORY.IT));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_fun), CATEGORY.FUN));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_entertainment), CATEGORY.ENTERTAINMENT));
        navigationLayout.addView(makeNavigationCategoryButton(getString(R.string.feed_game), CATEGORY.GAME));

        findViewById(R.id.MainNavigationLoginButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNavigationButtons();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        mDrawerLayout.closeDrawers();
        Fragment fragment;
        if (itemPosition == 0) {
            fragment = FeedFragment.newFragment(mSelectedCategory, TYPE.HOT);
            mSelectedType = TYPE.HOT;
        } else if (itemPosition == 1) {
            fragment = FeedFragment.newFragment(mSelectedCategory, TYPE.NEW);
            mSelectedType = TYPE.NEW;
        } else {
            return true;
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, fragment)
                .commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem unRead = menu.add(Menu.NONE, 1, Menu.NONE, R.string.type_read_later);
        unRead.setIcon(R.drawable.ic_action_labels);
        MenuItemCompat.setShowAsAction(unRead, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getTitle().equals(getString(R.string.type_read_later))) {
            startActivity(new Intent(this, ReadLaterActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            super.onBackPressed();
        }
    }

    private void loadNavigationButtons() {
        final LinearLayout navigationAdditionsLayout = (LinearLayout) findViewById(R.id.MainNavigationAdditions);
        navigationAdditionsLayout.removeAllViews();

        for (final String keyword : KeywordDAO.findAll()) {
            navigationAdditionsLayout.addView(makeNavigationButton(keyword, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TopActivity.this, KeywordSearchActivity.class);
                    startActivity(intent.putExtras(KeywordSearchActivity.buildBundle(keyword)));
                    mDrawerLayout.closeDrawers();
                }
            }, new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    buildDeleteDialog(keyword, v).show();
                    return false;
                }
            }));
        }
        for (final String userId : UserIdDAO.findAll()) {
            navigationAdditionsLayout.addView(makeNavigationButton(userId, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TopActivity.this, UserSearchActivity.class);
                    startActivity(intent.putExtras(UserSearchActivity.buildBundle(userId)));
                    mDrawerLayout.closeDrawers();
                }
            }, new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    buildDeleteUserIdDialog(userId, v).show();
                    return false;
                }
            }));
        }
    }

    private View makeNavigationCategoryButton(final String label, final CATEGORY category) {
        return makeNavigationButton(label, new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.containerFrameLayout, FeedFragment.newFragment(category, mSelectedType))
                        .commit();
                mDrawerLayout.closeDrawers();
                getSupportActionBar().setTitle(label);
                mSelectedCategory = category;
            }
        }, null);
    }

    private View makeNavigationButton(String label, OnClickListener onClick, OnLongClickListener onLongClick) {
        View navigationView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_top_navigation, null);
        TextView textView = (TextView) navigationView.findViewById(R.id.MainNavigationTextView);
        textView.setText(label);
        textView.setOnClickListener(onClick);
        textView.setOnLongClickListener(onLongClick);
        return navigationView;
    }

    private AlertDialog buildDeleteDialog(final String word, final View view) {
        DialogInterface.OnClickListener onPositiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                KeywordDAO.delete(word);
                view.setVisibility(View.GONE);
            }
        };

        return new AlertDialog.Builder(this)
                .setTitle(R.string.settings_ngword_delete)
                .setPositiveButton(android.R.string.ok, onPositiveListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private AlertDialog buildDeleteUserIdDialog(final String word, final View view) {
        DialogInterface.OnClickListener onPositiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserIdDAO.delete(word);
                view.setVisibility(View.GONE);
            }
        };

        return new AlertDialog.Builder(this)
                .setTitle(R.string.settings_ngword_delete)
                .setPositiveButton(android.R.string.ok, onPositiveListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
