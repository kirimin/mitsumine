package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine.R
import rx.subscriptions.CompositeSubscription
import java.net.URLEncoder
import javax.inject.Inject

class EntryInfoPresenter @Inject constructor(val useCase: EntryInfoUseCase) {

    private val subscriptions = CompositeSubscription()
    private lateinit var view: EntryInfoView

    fun onCreate(view: EntryInfoView, url: String) {
        this.view = view
        view.initActionBar()
        view.showProgressBar()
        subscriptions.add(useCase.requestEntryInfo(URLEncoder.encode(url, "utf-8"))
                .filter { !it.isNullObject }
                .flatMap { useCase.loadStars(entryInfo = it) }
                .subscribe ({ entryInfo ->
                    view.setEntryInfo(entryInfo)
                    view.hideProgressBar()
                    val commentList = entryInfo.bookmarkList.filter { it.hasComment }
                    val stars = commentList.sortedByDescending { it.stars?.allStarsCount }
                    view.setBookmarkFragments(entryInfo.bookmarkList, commentList, stars, entryInfo.entryId)
                    view.setCommentCount(commentList.count().toString())
                    if (useCase.isLogin()) {
                        view.setRegisterBookmarkFragment(entryInfo.url)
                    }
                    view.setViewPagerSettings(currentItem = 2, offscreenPageLimit = 3)
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