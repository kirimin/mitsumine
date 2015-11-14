package me.kirimin.mitsumine.presenter

import me.kirimin.mitsumine.view.AboutView

class AboutPresenter {

    var view: AboutView? = null

    fun onCreate(aboutView: AboutView) {
        view = aboutView
        view!!.initViews()
    }
}