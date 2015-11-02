package me.kirimin.mitsumine.presenter

import android.content.Context
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.network.api.EntryInfoApi
import me.kirimin.mitsumine.view.EntryInfoView
import me.kirimin.mitsumine.view.fragment.BookmarkListFragment
import me.kirimin.mitsumine.view.fragment.RegisterBookmarkFragment
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class EntryInfoPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: EntryInfoView? = null

    fun onCreate(entryInfoView: EntryInfoView, url: String, context: Context) {
        this.view = entryInfoView
        view!!.initActionBar()

        subscriptions.add(EntryInfoApi.request(context, url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { entryInfo -> !entryInfo.isNullObject() }
                .subscribe ({ entryInfo ->
                    view?.setEntryInfo(entryInfo)
                    view?.addPage(BookmarkListFragment.newFragment(entryInfo.bookmarkList), R.string.entry_info_all_bookmarks)

                    val commentList = entryInfo.bookmarkList.filter { bookmark -> bookmark.hasComment() }
                    view?.addPage(BookmarkListFragment.newFragment(commentList), R.string.entry_info_comments)
                    view?.setCommentCount(commentList.count().toString())

                    AccountDAO.get()?.let {
                        view?.addPage(RegisterBookmarkFragment.newFragment(entryInfo.url), R.string.entry_info_register_bookmark)
                    }
                    view?.setViewPagerSettings(1, 2)

                }, { view?.showNetworkErrorToast() }))
    }

    fun onDestroy() {
        view = null
    }
}