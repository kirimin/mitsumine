package me.kirimin.mitsumine.about

import android.content.Intent
import me.kirimin.mitsumine.R

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine._common.ui.BaseActivity

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(findViewById(R.id.tool_bar) as Toolbar)
        val actionBar = supportActionBar ?: return
        actionBar.setTitle(R.string.about_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        findViewById(R.id.aboutLicenseButton)?.setOnClickListener { startActivity(Intent(this@AboutActivity, LicenseActivity::class.java)) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }
}
