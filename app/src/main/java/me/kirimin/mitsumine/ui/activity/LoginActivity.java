package me.kirimin.mitsumine.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.network.api.oauth.OAuthApiManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final OAuthApiManager OAuthApiManager = new OAuthApiManager();
        findViewById(R.id.LoginAuthButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OAuthApiManager.requestAuthUrl()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String authUrl) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
                            }
                        });
            }
        });
        findViewById(R.id.LoginLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pinCodeEdit = (EditText) findViewById(R.id.LoginPinCodeEditText);
                OAuthApiManager.requestUserInfo(pinCodeEdit.getText().toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Account>() {
                            @Override
                            public void call(Account account) {
                                AccountDAO.save(account);
                            }
                        });
            }
        });
    }
}
