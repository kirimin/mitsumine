package me.kirimin.mitsumine.network.api;

import com.android.volley.RequestQueue;

import rx.Observable;

public class BookmarkCountApi {

    public static Observable<String> request(final RequestQueue requestQueue, final String url) {
        return ApiAccessor.stringRequest(requestQueue, "http://api.b.st-hatena.com/entry.count?url=" + url);
    }
}
