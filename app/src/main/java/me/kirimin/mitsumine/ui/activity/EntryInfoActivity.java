package me.kirimin.mitsumine.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.kirimin.mitsumine.R;

public class EntryInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_info);
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.entry_info_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
