package me.kirimin.mitsumine.about

import me.kirimin.mitsumine.R

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import me.kirimin.mitsumine.about.AboutPresenter
import me.kirimin.mitsumine.about.AboutView

public class AboutActivity : AppCompatActivity(), AboutView {

    val presenter = AboutPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        presenter.onCreate(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViews() {
        setSupportActionBar(findViewById(R.id.tool_bar) as Toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.about_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }
}
