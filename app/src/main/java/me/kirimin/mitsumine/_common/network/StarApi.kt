package me.kirimin.mitsumine._common.network

import android.content.Context
import me.kirimin.mitsumine._common.domain.model.Star
import me.kirimin.mitsumine._common.network.parser.StarApiParser
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object StarApi {

    fun requestCommentStar(context: Context, userId: String, timestamp: String, entryId: String): Observable<List<Star>> {
        val date = timestamp.replace("/", "")
        val url = "http://s.hatena.com/entry.json?uri=http://b.hatena.ne.jp/$userId/$date%23bookmark-$entryId"
        return ApiAccessor.request(context, url)
                .map { StarApiParser.parseResponse(it) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}