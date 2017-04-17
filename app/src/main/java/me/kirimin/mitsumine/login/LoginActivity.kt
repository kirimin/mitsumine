package me.kirimin.mitsumine.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.network.oauth.HatenaOAuth
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.settings_login)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        val OAuthApiManager = HatenaOAuth()
        authButton.setOnClickListener { v ->
            v.isEnabled = false
            subscriptions.add(OAuthApiManager.requestAuthUrl()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ authUrl ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)))
                        v.isEnabled = true
                    }, {
                        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
                        v.isEnabled = true
                    })
            )
        }
        loginButton.setOnClickListener {
            subscriptions.add(OAuthApiManager.requestUserInfo(pinCodeEditText.getText().toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ account ->
                        AccountDAO.save(account)
                        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        finish()
                    }, { e ->
                        Toast.makeText(applicationContext, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                    })
            )
        }
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
