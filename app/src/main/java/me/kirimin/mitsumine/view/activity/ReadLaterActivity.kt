package me.kirimin.mitsumine.view.activity

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.view.fragment.ReadLaterFeedFragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_common_container.*

class ReadLaterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_container)

        toolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.type_read_later)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        supportFragmentManager.beginTransaction().replace(R.id.containerFrameLayout, ReadLaterFeedFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
