package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine.R
import rx.subscriptions.CompositeSubscription
import java.net.URLEncoder

class EntryInfoPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: EntryInfoView? = null
    private lateinit var useCase: EntryInfoUseCase

    fun onCreate(entryInfoView: EntryInfoView, useCase: EntryInfoUseCase, url: String) {
        this.view = entryInfoView
        this.useCase = useCase
        entryInfoView.initActionBar()
        subscriptions.add(useCase.requestEntryInfo(URLEncoder.encode(url, "utf-8"))
                .filter { !it.isNullObject }
                .subscribe ({ entryInfo ->
                    val view = view ?: return@subscribe
                    view.setEntryInfo(entryInfo)
                    val commentList = entryInfo.bookmarkList.filter { it.hasComment }
                    view.setBookmarkFragments(entryInfo.bookmarkList, commentList, entryInfo.entryId)
                    view.setCommentCount(commentList.count().toString())
                    if (useCase.isLogin()) {
                        view.setRegisterBookmarkFragment(entryInfo.url)
                    }
                    view.setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
                }, { view?.showError(R.string.network_error) })
        )
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
        view = null
    }

}