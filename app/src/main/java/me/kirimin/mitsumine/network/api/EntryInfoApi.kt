package me.kirimin.mitsumine.network.api

import android.content.Context
import me.kirimin.mitsumine.model.EntryInfo
import me.kirimin.mitsumine.network.api.parser.EntryInfoApiParser

import rx.Observable

public class EntryInfoApi {
    companion object {

        private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

        public fun request(context: Context, url: String): Observable<EntryInfo> {
            return ApiAccessor.request(context, REQUEST_URL + url)
                    .map { response -> EntryInfoApiParser.parseResponse(response) }
        }
    }
}