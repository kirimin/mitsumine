package me.kirimin.mitsumine.network.api

import com.android.volley.RequestQueue

import org.json.JSONObject

import rx.Observable

public class EntryInfoApi {
    companion object {

        private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

        public fun request(requestQueue: RequestQueue, url: String): Observable<JSONObject> {
            return ApiAccessor.request(requestQueue, REQUEST_URL + url)
        }
    }
}