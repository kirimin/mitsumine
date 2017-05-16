package me.kirimin.mitsumine.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_license.*
import me.kirimin.mitsumine.MyApplication
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.ui.BaseActivity

class LicenseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
        licenseWebView.loadUrl("file:///android_asset/license.html")
    }

    override fun onDestroy() {
        licenseWebView.destroy()
        super.onDestroy()
    }

    override fun injection() {
        (application as MyApplication).getApplicationComponent().inject(this)
    }
}