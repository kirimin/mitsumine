package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class EntryInfoPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: EntryInfoView? = null
    private lateinit var repository: EntryInfoRepository

    fun onCreate(entryInfoView: EntryInfoView, repository: EntryInfoRepository, url: String, context: Context) {
        this.view = entryInfoView
        this.repository = repository
        entryInfoView.initActionBar()
        subscriptions.add(repository.requestEntryInfoApi(context, url)
                .filter { !it.isNullObject() }
                .subscribe ({ entryInfo ->
                    val view = view ?: return@subscribe
                    view.setEntryInfo(entryInfo)
                    val commentList = entryInfo.bookmarkList.filter { it.hasComment() }
                    view.setBookmarkFragments(entryInfo.bookmarkList, commentList)
                    view.setCommentCount(commentList.count().toString())
                    if (repository.isLogin()) {
                        view.setRegisterBookmarkFragment(entryInfo.url)
                    }
                    view.setViewPagerSettings(currentItem = 1, offscreenPageLimit = 2)
                }, {
                    view?.showNetworkErrorToast()
                })
        )
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
        view = null
    }

}