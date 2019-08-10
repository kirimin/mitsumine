package me.kirimin.mitsumine.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.network.oauth.HatenaOAuth
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.main.activity_login.*
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine._common.ui.BaseActivity

class LoginActivity : BaseActivity() {

    private val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.settings_login)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        val oAuthApiManager = HatenaOAuth()
        authButton.setOnClickListener { v ->
            v.isEnabled = false
            subscriptions.add(oAuthApiManager.requestAuthUrl()
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
            subscriptions.add(oAuthApiManager.requestUserInfo(pinCodeEditText.text.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ account ->
                        AccountDAO.save(account)
                        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        finish()
                    }, {
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

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }
}
