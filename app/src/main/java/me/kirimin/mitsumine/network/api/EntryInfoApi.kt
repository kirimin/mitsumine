package me.kirimin.mitsumine.network.api

import com.android.volley.RequestQueue
import me.kirimin.mitsumine.model.EntryInfo
import me.kirimin.mitsumine.network.api.parser.EntryInfoApiParser

import rx.Observable

public class EntryInfoApi {
    companion object {

        private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

        public fun request(requestQueue: RequestQueue, url: String): Observable<EntryInfo> {
            return ApiAccessor.request(requestQueue, REQUEST_URL + url)
                    .map { response -> EntryInfoApiParser.parseResponse(response) }
        }
    }
}