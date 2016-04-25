package me.kirimin.mitsumine._common.network

import android.content.Context
import rx.Observable

object BookmarkCountApi {

    fun request(context: Context, url: String): Observable<String> {
        return ApiAccessor.stringRequest(context, "http://api.b.st-hatena.com/entry.count?url=" + url)
    }
}
