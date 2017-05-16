package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine.R
import rx.subscriptions.CompositeSubscription
import java.net.URLEncoder

class EntryInfoPresenter {

    private val subscriptions = CompositeSubscription()
    private lateinit var view: EntryInfoView
    private lateinit var useCase: EntryInfoUseCase

    fun onCreate(view: EntryInfoView, useCase: EntryInfoUseCase, url: String) {
        this.view = view
        this.useCase = useCase
        view.initActionBar()
        view.showProgressBar()
        subscriptions.add(useCase.requestEntryInfo(URLEncoder.encode(url, "utf-8"))
                .filter { !it.isNullObject }
                .subscribe ({ entryInfo ->
                    view.setEntryInfo(entryInfo)
                    view.hideProgressBar()
                    val commentList = entryInfo.bookmarkList.filter { it.hasComment }
                    view.setBookmarkFragments(entryInfo.bookmarkList, commentList, entryInfo.entryId)
                    view.setCommentCount(commentList.count().toString())
                    if (useCase.isLogin()) {
                        view.setRegisterBookmarkFragment(entryInfo.url)
                    }
                    view.setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
                }, {
                    view.hideProgressBar()
                    view.showError(R.string.network_error)
                })
        )
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
    }
}