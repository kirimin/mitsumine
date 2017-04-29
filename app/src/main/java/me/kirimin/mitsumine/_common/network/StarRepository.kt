package me.kirimin.mitsumine._common.network

import me.kirimin.mitsumine._common.domain.model.Star
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object StarRepository {

    fun requestCommentStar(userId: String, timestamp: String, entryId: String): Observable<Int> {
        val date = timestamp.replace("/", "")
        val uri = "http://b.hatena.ne.jp/$userId/$date%23bookmark-$entryId"
        return Client.default(Client.EndPoint.STAR).build()
                .create(HatenaBookmarkService::class.java)
                .starOfBookmark(uri)
                .map { Star(it).allStarsCount }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}