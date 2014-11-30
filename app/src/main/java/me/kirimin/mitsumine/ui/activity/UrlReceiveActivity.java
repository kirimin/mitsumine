package me.kirimin.mitsumine.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class UrlReceiveActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
            String url = getIntent().getExtras().getCharSequence(Intent.EXTRA_TEXT).toString();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://b.hatena.ne.jp/entry/" + url)));
        }
        finish();
    }
}
