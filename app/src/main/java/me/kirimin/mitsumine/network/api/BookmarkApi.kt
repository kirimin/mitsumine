package me.kirimin.mitsumine.network.api

import org.json.JSONException
import org.json.JSONObject
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.oauth.OAuthService

import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.network.ApiRequestException
import me.kirimin.mitsumine.network.api.oauth.Consumer
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthProvider
import me.kirimin.mitsumine.network.api.parser.BookmarkApiParser
import rx.Observable
import rx.Subscriber

public class BookmarkApi {
    companion object {

        public fun requestAddBookmark(url: String, account: Account, comment: String, tags: List<String>, isPrivate: Boolean, isTwitter: Boolean): Observable<Bookmark?> {
            return Observable.create<Bookmark?>{ subscriber ->
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
                if (response.getCode() == 200) {
                    try {
                        val json = JSONObject(response.getBody())
                        subscriber.onNext(BookmarkApiParser.mapToMyBookmarkInfo(json))
                        subscriber.onCompleted()
                    } catch (e: JSONException) {
                        subscriber.onError(ApiRequestException("Json exception."))
                    }
                } else {
                    subscriber.onError(ApiRequestException("error code:" + response.getCode()))
                }
            }
        }

        public fun requestDeleteBookmark(url: String, account: Account): Observable<Boolean> {
            return Observable.create<Boolean>{ subscriber ->
                val request = OAuthRequest(Verb.DELETE, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.getCode() == 204) {
                    subscriber.onNext(true)
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(ApiRequestException("error code:" + response.getCode()))
                }
            }
        }

        public fun requestBookmarkInfo(url: String, account: Account): Observable<Bookmark?> {
            return Observable.create<Bookmark?>{ subscriber ->
                val request = OAuthRequest(Verb.GET, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.getCode() == 200) {
                    try {
                        val json = JSONObject(response.getBody())
                        subscriber.onNext(BookmarkApiParser.mapToMyBookmarkInfo(json))
                        subscriber.onCompleted()
                    } catch (e: JSONException) {
                        subscriber.onError(ApiRequestException(""))
                    }
                } else if (response.getCode() == 404) {
                    subscriber.onNext(null)
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(ApiRequestException(""))
                }
            }
        }
    }
}