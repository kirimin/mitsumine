package me.kirimin.mitsumine._common.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity(), BaseViewInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injection()
    }

    override fun showError(res: Int) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
    }

    abstract fun injection()
}