package me.kirimin.mitsumine.network.api

import me.kirimin.mitsumine.network.api.parser.TagListApiParser
import rx.Observable

public class TagListApi {
    companion object {

        private val REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url="

        public fun request(url: String): Observable<List<String>> {
            return ApiAccessor.request(REQUEST_URL + url)
                    .map { response -> TagListApiParser.parseResponse(response) }
        }
    }
}
