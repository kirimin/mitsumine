package me.kirimin.mitsumine.presenter

import android.content.Context
import me.kirimin.mitsumine.domain.EntryInfoUseCase
import me.kirimin.mitsumine.model.EntryInfo
import me.kirimin.mitsumine.view.EntryInfoView
import rx.Subscriber

class EntryInfoPresenter :  Subscriber<EntryInfo>() {

    private var view: EntryInfoView? = null
    private var useCase: EntryInfoUseCase? = null

    fun onCreate(entryInfoView: EntryInfoView, entryInfoUseCase: EntryInfoUseCase, url: String, context: Context) {
        this.view = entryInfoView
        this.useCase = entryInfoUseCase

        view!!.initActionBar()
        useCase!!.requestEntryInfo(url, context, this)
    }

    fun onDestroy() {
        useCase?.unSubscribe()
        view = null
    }

    override fun onNext(entryInfo: EntryInfo) {
        view?.setEntryInfo(entryInfo)
        val commentList = useCase!!.getHasCommentBookmarks(entryInfo.bookmarkList)
        view?.setBookmarkFragments(entryInfo.bookmarkList, commentList)
        view?.setCommentCount(commentList.count().toString())
        if (useCase!!.isLogin()) {
            view?.setRegisterBookmarkFragment(entryInfo.url)
        }
        view?.setViewPagerSettings(1, 2)
    }

    override fun onError(e: Throwable) {
        view?.showNetworkErrorToast()
    }

    override fun onCompleted() {
    }

}