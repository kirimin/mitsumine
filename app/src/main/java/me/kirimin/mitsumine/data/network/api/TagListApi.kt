package me.kirimin.mitsumine.data.network.api

import android.content.Context
import me.kirimin.mitsumine.data.network.api.parser.TagListApiParser
import rx.Observable

public class TagListApi {
    companion object {

        private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

        public fun request(context: Context, url: String): Observable<List<String>> {
            return ApiAccessor.request(context, REQUEST_URL + url)
                    .map { response -> TagListApiParser.parseResponse(response) }
        }
    }
}
