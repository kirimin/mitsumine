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
import me.kirimin.mitsumine.network.api.oauth.Consumer
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthProvider
import rx.Observable
import rx.Subscriber

public class MyBookmarksApi {
    companion object {

        public fun request(account: Account, keyword: String, offset: Int): Observable<JSONObject> {
            return Observable.create<JSONObject>{ subscriber ->
                val oAuthService = ServiceBuilder().provider(javaClass<HatenaOAuthProvider>()).apiKey(Consumer.K).apiSecret(Consumer.S).build()
                val accessToken = Token(account.token, account.tokenSecret)
                val request = OAuthRequest(Verb.GET, "http://b.hatena.ne.jp/" + account.urlName + "/search/json")
                if (!keyword.isEmpty()) {
                    request.addQuerystringParameter("q", keyword)
                }
                request.addQuerystringParameter("of", offset.toString())
                request.addQuerystringParameter("limit", "50")
                oAuthService.signRequest(accessToken, request)
                val response = request.send()
                val body = response.getBody()
                try {
                    subscriber.onNext(JSONObject(body))
                    subscriber.onCompleted()
                } catch (e: JSONException) {
                    subscriber.onError(e)
                }

            }
        }
    }
}
