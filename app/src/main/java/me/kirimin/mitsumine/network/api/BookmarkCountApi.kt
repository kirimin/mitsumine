package me.kirimin.mitsumine.network.api

import rx.Observable

public class BookmarkCountApi {
    companion object {

        public fun request(url: String): Observable<String> {
            return ApiAccessor.stringRequest("http://api.b.st-hatena.com/entry.count?url=" + url)
        }
    }
}
