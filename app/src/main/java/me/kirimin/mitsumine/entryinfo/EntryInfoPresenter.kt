package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class EntryInfoPresenter : Subscriber<EntryInfo>() {

    private val subscriptions = CompositeSubscription()
    private var view: EntryInfoView? = null
    private lateinit var repository: EntryInfoRepository

    fun onCreate(view: EntryInfoView, repository: EntryInfoRepository, url: String, context: Context) {
        this.view = view
        this.repository = repository

        view.initActionBar()
        subscriptions.add(repository.requestEntryInfoApi(context, url)
                .filter { entryInfo -> !entryInfo.isNullObject() }
                .subscribe (this))
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
        view = null
    }

    override fun onNext(entryInfo: EntryInfo) {
        val view = view ?: return
        view.setEntryInfo(entryInfo)
        val commentList = entryInfo.bookmarkList.filter { bookmark -> bookmark.hasComment() }
        view.setBookmarkFragments(entryInfo.bookmarkList, commentList)
        view.setCommentCount(commentList.count().toString())
        if (repository.isLogin()) {
            view.setRegisterBookmarkFragment(entryInfo.url)
        }
        view.setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
    }

    override fun onError(e: Throwable) {
        view?.showNetworkErrorToast()
    }

    override fun onCompleted() {
    }

}