package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.domain.model.Stars
import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class StarRepository @Inject constructor() {

    fun requestCommentStar(userId: String, timestamp: String, entryId: String): Observable<Stars> {
        val date = timestamp.replace("/", "")
        val uri = "http://b.hatena.ne.jp/$userId/$date%23bookmark-$entryId"
        return Client.default(Client.EndPoint.STAR).build()
                .create(HatenaBookmarkService::class.java)
                .starOfBookmark(uri)
                .map(::Stars)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}