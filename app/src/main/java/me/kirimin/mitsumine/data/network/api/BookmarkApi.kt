package me.kirimin.mitsumine.data.network.api

import org.json.JSONException
import org.json.JSONObject
import org.scribe.model.OAuthRequest
import org.scribe.model.Verb

import me.kirimin.mitsumine.domain.model.Account
import me.kirimin.mitsumine.domain.model.Bookmark
import me.kirimin.mitsumine.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine.data.network.api.parser.BookmarkApiParser
import rx.Observable

public class BookmarkApi {
    companion object {

        public fun requestAddBookmark(url: String, account: Account, comment: String, tags: List<String>, isPrivate: Boolean, isTwitter: Boolean): Observable<Bookmark?> {
            return Observable.create<JSONObject>{ subscriber ->
                val request = OAuthRequest(Verb.POST, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                request.addQuerystringParameter("comment", comment)
                for (tag in tags) {
                    request.addQuerystringParameter("tags", tag)
                }
                if (isPrivate) {
                    request.addQuerystringParameter("private", "true")
                }
                if (isTwitter) {
                    request.addQuerystringParameter("post_twitter", "true")
                }
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.code == 200) {
                    try {
                        subscriber.onNext(JSONObject(response.body))
                        subscriber.onCompleted()
                    } catch (e: JSONException) {
                        subscriber.onError(ApiRequestException("Json exception."))
                    }
                } else {
                    subscriber.onError(ApiRequestException("error code:" + response.code))
                }
            }.map { response -> BookmarkApiParser.parseResponse(response) }
        }

        public fun requestDeleteBookmark(url: String, account: Account): Observable<Boolean> {
            return Observable.create<Boolean>{ subscriber ->
                val request = OAuthRequest(Verb.DELETE, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.code == 204) {
                    subscriber.onNext(true)
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(ApiRequestException("error code:" + response.code))
                }
            }
        }

        public fun requestBookmarkInfo(url: String, account: Account): Observable<Bookmark?> {
            return Observable.create<JSONObject>{ subscriber ->
                val request = OAuthRequest(Verb.GET, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.code == 200) {
                    try {
                        subscriber.onNext(JSONObject(response.getBody()))
                        subscriber.onCompleted()
                    } catch (e: JSONException) {
                        subscriber.onError(ApiRequestException(""))
                    }
                } else if (response.code == 404) {
                    subscriber.onNext(null)
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(ApiRequestException(""))
                }
            }.map { response -> if (response != null) BookmarkApiParser.parseResponse(response) else null }
        }
    }
}