package me.kirimin.mitsumine.view.activity

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.database.NGWordDAO
import me.kirimin.mitsumine.domain.model.Account

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

import kotlinx.android.synthetic.activity_settings.*
import me.kirimin.mitsumine.view.fragment.SettingFragment

public class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar.setTitle(R.string.settings_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        fragmentManager.beginTransaction().replace(R.id.content, SettingFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}