package me.kirimin.mitsumine.domain

import me.kirimin.mitsumine.data.MyBookmarksData
import me.kirimin.mitsumine.domain.model.MyBookmark
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class MyBookmarksUseCase(val data: MyBookmarksData) {

    private val subscriptions = CompositeSubscription()

    fun requestMyBookmarks(subscriber: Observer<List<MyBookmark>>, keyword: String, offset: Int) {
        subscriptions.add(data.requestMyBookmarks(keyword, offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(subscriber))
    }
}