package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import rx.Subscriber

class EntryInfoPresenter :  Subscriber<EntryInfo>() {

    private var view: EntryInfoView? = null
    private lateinit var useCase: EntryInfoUseCase

    fun onCreate(entryInfoView: EntryInfoView, entryInfoUseCase: EntryInfoUseCase, url: String, context: Context) {
        this.view = entryInfoView
        this.useCase = entryInfoUseCase

        entryInfoView.initActionBar()
        entryInfoUseCase.requestEntryInfo(url, context, this)
    }

    fun onDestroy() {
        useCase.unSubscribe()
        view = null
    }

    override fun onNext(entryInfo: EntryInfo) {
        val view = view ?: return
        view.setEntryInfo(entryInfo)
        val commentList = useCase.getHasCommentBookmarks(entryInfo.bookmarkList)
        view.setBookmarkFragments(entryInfo.bookmarkList, commentList)
        view.setCommentCount(commentList.count().toString())
        if (useCase.isLogin()) {
            view.setRegisterBookmarkFragment(entryInfo.url)
        }
        view.setViewPagerSettings(1, 2)
    }

    override fun onError(e: Throwable) {
        view?.showNetworkErrorToast()
    }

    override fun onCompleted() {
    }

}