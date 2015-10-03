package me.kirimin.mitsumine.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity

import me.kirimin.mitsumine.R

public class UrlReceiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Intent.ACTION_SEND == intent.action) {
            val url = intent.extras.getCharSequence(Intent.EXTRA_TEXT).toString().replace("#", "%23")
            val pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            if (pref.getBoolean(getString(R.string.key_use_browser_to_comment_list), false)) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://b.hatena.ne.jp/entry/" + url)))
            } else {
                val intent = Intent(applicationContext, EntryInfoActivity::class.java)
                intent.putExtras(EntryInfoActivity.buildBundle(url))
                startActivity(intent)
            }
        }
        finish()
    }
}
