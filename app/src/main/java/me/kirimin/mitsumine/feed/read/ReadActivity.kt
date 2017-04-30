package me.kirimin.mitsumine.feed.read

import me.kirimin.mitsumine.R
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_common_container.*
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine._common.ui.BaseActivity

class ReadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_container)

        toolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.read_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerFrameLayout, ReadFeedFragment())
                .commit()
    }

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
