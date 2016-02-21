package me.kirimin.mitsumine.view.activity

import me.kirimin.mitsumine.R

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import me.kirimin.mitsumine.view.fragment.SettingFragment

public class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.settings_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        fragmentManager.beginTransaction().replace(R.id.content, SettingFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}