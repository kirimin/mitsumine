package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class BookmarkCountRepository @Inject constructor() {

    fun requestBookmarkCount(url: String): Observable<String> =
            Client.default(Client.EndPoint.BOOKMARK_COUNT).build().create(HatenaBookmarkService::class.java).bookmarkCount(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
}