package me.kirimin.mitsumine.ui.activity

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.ui.fragment.ReadLaterFeedFragment

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import kotlinx.android.synthetic.activity_common_container.*

public class ReadLaterActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_container)

        toolBar.setBackgroundColor(getResources().getColor(R.color.orange))
        setSupportActionBar(toolBar)
        val actionBar = getSupportActionBar()
        actionBar.setTitle(R.string.type_read_later)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, ReadLaterFeedFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
