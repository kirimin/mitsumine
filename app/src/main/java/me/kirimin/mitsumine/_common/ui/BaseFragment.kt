package me.kirimin.mitsumine._common.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

abstract class BaseFragment:  Fragment(), BaseViewInterface {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injection()
    }

    override fun showError(res: Int) {
        val baseActivity = activity as? BaseActivity ?: return
        baseActivity.showError(res)
    }

    abstract fun injection()
}