package me.kirimin.mitsumine.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuth
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.activity_login.*

public class LoginActivity : ActionBarActivity() {

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolBar)
        val actionBar = getSupportActionBar()
        actionBar.setTitle(R.string.settings_login)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        val OAuthApiManager = HatenaOAuth()
        authButton.setOnClickListener { v ->
            v.setEnabled(false)
            subscriptions.add(OAuthApiManager.requestAuthUrl()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ authUrl ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)))
                        v.setEnabled(true)
                    }, { e ->
                        v.setEnabled(true)
                    })
            )
        }
        loginButton.setOnClickListener {
            subscriptions.add(OAuthApiManager.requestUserInfo(pinCodeEditText.getText().toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ account ->
                        AccountDAO.save(account)
                        Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        finish()
                    }, { e ->
                        Toast.makeText(getApplicationContext(), getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                    })
            )
        }
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
