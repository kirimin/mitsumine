package me.kirimin.mitsumine.common.network

import android.content.Context
import me.kirimin.mitsumine.common.network.parser.TagListApiParser
import rx.Observable

object TagListApi {

    private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

    fun request(context: Context, url: String): Observable<List<String>> {
        return ApiAccessor.request(context, REQUEST_URL + url)
                .map { response -> TagListApiParser.parseResponse(response) }
    }
}
