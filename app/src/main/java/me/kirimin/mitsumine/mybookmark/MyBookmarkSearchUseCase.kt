package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine.mybookmark.MyBookmarkSearchRepository
import me.kirimin.mitsumine.common.domain.model.MyBookmark
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class MyBookmarkSearchUseCase(val repository: MyBookmarkSearchRepository) {

    private val subscriptions = CompositeSubscription()

    fun requestMyBookmarks(subscriber: Observer<List<MyBookmark>>, keyword: String, offset: Int) {
        subscriptions.add(repository.requestMyBookmarks(keyword, offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(subscriber))
    }
}