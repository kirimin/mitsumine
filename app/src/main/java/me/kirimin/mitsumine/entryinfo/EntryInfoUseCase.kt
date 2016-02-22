package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine.common.database.AccountDAO
import me.kirimin.mitsumine.common.domain.model.Bookmark
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

open class EntryInfoUseCase {

    private val subscriptions = CompositeSubscription()
    private val repository: EntryInfoRepository

    constructor(entryInfoRepository: EntryInfoRepository) {
        this.repository = entryInfoRepository
    }

    open fun requestEntryInfo(url: String, context: Context, subscriber: Subscriber<EntryInfo>) {
        subscriptions.add(repository.requestEntryInfoApi(context, url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { entryInfo -> !entryInfo.isNullObject() }
                .subscribe (subscriber))
    }

    open fun unSubscribe() {
        subscriptions.unsubscribe()
    }

    open fun getHasCommentBookmarks(bookmarkList: List<Bookmark>): List<Bookmark> {
        return bookmarkList.filter { bookmark -> bookmark.hasComment() }
    }

    open fun isLogin(): Boolean {
        return AccountDAO.get() != null
    }
}
