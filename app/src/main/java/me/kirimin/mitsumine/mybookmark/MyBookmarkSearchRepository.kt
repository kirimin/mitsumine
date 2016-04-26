package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.network.MyBookmarksApi
import me.kirimin.mitsumine._common.domain.model.MyBookmark
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MyBookmarkSearchRepository {
    fun requestMyBookmarks(keyword: String, offset: Int): Observable<MyBookmark> = MyBookmarksApi.request(AccountDAO.get()!!, keyword, offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
}