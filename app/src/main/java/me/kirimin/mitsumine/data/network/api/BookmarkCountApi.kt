package me.kirimin.mitsumine.data.network.api

import android.content.Context
import rx.Observable

object BookmarkCountApi {

    fun request(context: Context, url: String): Observable<String> {
        return ApiAccessor.stringRequest(context, "http://api.b.st-hatena.com/entry.count?url=" + url)
    }
}
