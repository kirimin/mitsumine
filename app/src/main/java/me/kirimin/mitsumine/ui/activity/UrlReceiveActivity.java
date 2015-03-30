package me.kirimin.mitsumine.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import me.kirimin.mitsumine.R;

public class UrlReceiveActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
            String url = getIntent().getExtras().getCharSequence(Intent.EXTRA_TEXT).toString().replace("#", "%23");
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (pref.getBoolean(getString(R.string.key_use_browser_to_comment_list), false)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://b.hatena.ne.jp/entry/" + url)));
            } else {
                Intent intent = new Intent(getApplicationContext(), EntryInfoActivity.class);
                intent.putExtras(EntryInfoActivity.Companion.buildBundle(url));
                startActivity(intent);
            }
        }
        finish();
    }
}
