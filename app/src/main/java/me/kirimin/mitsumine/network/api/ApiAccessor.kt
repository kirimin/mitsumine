package me.kirimin.mitsumine.network.api

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest

import org.json.JSONObject
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.oauth.OAuthService

import java.util.concurrent.ExecutionException

import me.kirimin.mitsumine.model.Account
import me.kirimin.mitsumine.network.ApiRequestException
import me.kirimin.mitsumine.network.api.oauth.Consumer
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthProvider
import rx.Observable
import rx.Subscriber

class ApiAccessor {
    companion object {

        fun request(requestQueue: RequestQueue, url: String): Observable<JSONObject> {
            return Observable.create<JSONObject>{ subscriber ->
                val future = RequestFuture.newFuture<JSONObject>()
                requestQueue.add<JSONObject>(JsonObjectRequest(url, null, future, future))
                try {
                    val response = future.get()
                    subscriber.onNext(response)
                    subscriber.onCompleted()
                } catch (e: InterruptedException) {
                    subscriber.onError(ApiRequestException("InterruptedException"))
                } catch (e: ExecutionException) {
                    subscriber.onError(ApiRequestException("ExecutionException"))
                }
            }
        }

        fun stringRequest(requestQueue: RequestQueue, url: String): Observable<String> {
            return Observable.create<String>{ subscriber ->
                val future = RequestFuture.newFuture<String>()
                requestQueue.add<String>(StringRequest(url, future, future))
                try {
                    val response = future.get()
                    subscriber.onNext(response)
                    subscriber.onCompleted()
                } catch (e: InterruptedException) {
                    subscriber.onError(ApiRequestException("InterruptedException"))
                } catch (e: ExecutionException) {
                    subscriber.onError(ApiRequestException("ExecutionException"))
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