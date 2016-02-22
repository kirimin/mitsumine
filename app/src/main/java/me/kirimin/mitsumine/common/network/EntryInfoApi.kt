package me.kirimin.mitsumine.common.network

import android.content.Context
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import me.kirimin.mitsumine.common.network.parser.EntryInfoApiParser
import org.json.JSONObject

import rx.Observable

object EntryInfoApi {

    private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

    fun request(context: Context, url: String): Observable<EntryInfo> {
        return ApiAccessor.request(context, REQUEST_URL + url)
                .map { response -> EntryInfoApiParser.parseResponse(response) }
    }
}