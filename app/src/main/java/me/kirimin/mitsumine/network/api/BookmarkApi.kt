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
import me.kirimin.mitsumine.network.ApiRequestException
import me.kirimin.mitsumine.network.api.oauth.Consumer
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthProvider
import rx.Observable
import rx.Subscriber

public class BookmarkApi {
    companion object {

        public fun requestAddBookmark(url: String, account: Account, comment: String, tags: List<String>, isPrivate: Boolean): Observable<JSONObject> {
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
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.getCode() != 200) {
                    subscriber.onError(ApiRequestException("error code:" + response.getCode()))
                } else {
                    try {
                        subscriber.onNext(JSONObject(response.getBody()))
                        subscriber.onCompleted()
                    } catch (e: JSONException) {
                        subscriber.onError(ApiRequestException("Json exception."))
                    }
                }
            }
        }

        public fun requestDeleteBookmark(url: String, account: Account): Observable<Boolean> {
            return Observable.create<Boolean>{ subscriber ->
                val request = OAuthRequest(Verb.DELETE, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.getCode() != 204) {
                    subscriber.onError(ApiRequestException("error code:" + response.getCode()))
                } else {
                    subscriber.onNext(true)
                    subscriber.onCompleted()
                }
            }
        }

        public fun requestBookmarkInfo(url: String, account: Account): Observable<JSONObject> {
            return Observable.create<JSONObject>{ subscriber ->
                val request = OAuthRequest(Verb.GET, "http://api.b.hatena.ne.jp/1/my/bookmark")
                request.addQuerystringParameter("url", url)
                val response = ApiAccessor.oAuthRequest(account, request)
                if (response.getCode() == 200) {
                    try {
                        subscriber.onNext(JSONObject(response.getBody()))
                        subscriber.onCompleted()
                    } catch (e: JSONException) {
                        subscriber.onError(ApiRequestException(""))
                    }

                } else if (response.getCode() == 404) {
                    subscriber.onNext(JSONObject())
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(ApiRequestException(""))
                }
            }
        }
    }
}