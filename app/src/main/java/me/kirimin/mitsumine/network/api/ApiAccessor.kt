package me.kirimin.mitsumine.network.api

import com.squareup.okhttp.CacheControl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

import org.json.JSONObject
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token

import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.network.ApiRequestException
import me.kirimin.mitsumine.network.api.oauth.Consumer
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthProvider
import rx.Observable
import java.util.concurrent.TimeUnit

class ApiAccessor {
    companion object {

        fun request(url: String): Observable<JSONObject> {
            return stringRequest(url).map { response ->
                JSONObject(response)
            }
        }

        fun stringRequest(url: String): Observable<String> {
            return Observable.create<String> { subscriber ->
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(url)
                        .cacheControl(CacheControl.Builder().maxAge(5, TimeUnit.MINUTES).build())
                        .get()
                        .build();
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = response.body().string()
                    subscriber.onNext(json)
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(ApiRequestException("error:" + response.code()));
                }
            }
        }

        fun oAuthRequest(account: Account, request: OAuthRequest): Response {
            val oAuthService = ServiceBuilder().provider(HatenaOAuthProvider::class.java).apiKey(Consumer.K).apiSecret(Consumer.S).build()
            val accessToken = Token(account.token, account.tokenSecret)
            oAuthService.signRequest(accessToken, request)
            return request.send()
        }
    }
}