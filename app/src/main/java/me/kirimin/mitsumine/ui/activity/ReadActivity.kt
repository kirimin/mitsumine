package me.kirimin.mitsumine.ui.activity

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.ui.fragment.ReadFeedFragment
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import kotlinx.android.synthetic.activity_common_container.*

public class ReadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_container)

        toolBar.setBackgroundColor(getResources().getColor(R.color.blue))
        setSupportActionBar(toolBar)
        val actionBar = getSupportActionBar()
        actionBar.setTitle(R.string.read_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFrameLayout, ReadFeedFragment())
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
