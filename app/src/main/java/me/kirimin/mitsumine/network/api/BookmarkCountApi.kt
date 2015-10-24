package me.kirimin.mitsumine.network.api

import android.content.Context
import rx.Observable

public class BookmarkCountApi {
    companion object {

        public fun request(context: Context, url: String): Observable<String> {
            return ApiAccessor.stringRequest(context, "http://api.b.st-hatena.com/entry.count?url=" + url)
        }
    }
}
