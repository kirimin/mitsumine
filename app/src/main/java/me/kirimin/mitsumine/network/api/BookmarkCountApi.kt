package me.kirimin.mitsumine.network.api

import com.android.volley.RequestQueue

import rx.Observable

public class BookmarkCountApi {
    companion object {

        public fun request(requestQueue: RequestQueue, url: String): Observable<String> {
            return ApiAccessor.stringRequest(requestQueue, "http://api.b.st-hatena.com/entry.count?url=" + url)
        }
    }
}
