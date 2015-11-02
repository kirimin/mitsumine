package me.kirimin.mitsumine.domain.usecase

import android.content.Context
import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.network.api.EntryInfoApi
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.model.EntryInfo
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

open class EntryInfoUseCase {

    private val subscriptions = CompositeSubscription()

    open fun requestEntryInfo(url: String, context: Context, subscriber: Subscriber<EntryInfo>) {
        subscriptions.add(EntryInfoApi.request(context, url)
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
