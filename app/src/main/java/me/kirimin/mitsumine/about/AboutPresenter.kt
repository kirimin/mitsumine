package me.kirimin.mitsumine.about

import me.kirimin.mitsumine.about.AboutView

class AboutPresenter {

    var view: AboutView? = null

    fun onCreate(aboutView: AboutView) {
        view = aboutView
        view!!.initViews()
    }
}