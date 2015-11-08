package me.kirimin.mitsumine.data.network.api.oauth

import org.json.JSONException
import org.json.JSONObject
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService

import me.kirimin.mitsumine.domain.model.Account
import me.kirimin.mitsumine.domain.exceptions.ApiRequestException
import rx.Observable
import rx.Subscriber

public class HatenaOAuth {

    private val oAuthService: OAuthService
    private var requestToken: Token? = null

    init {
        oAuthService = ServiceBuilder().provider(HatenaOAuthProvider::class.java).apiKey(Consumer.K).apiSecret(Consumer.S).build()
    }

    public fun requestAuthUrl(): Observable<String> {
        return Observable.create<String>{ subscriber ->
            requestToken = oAuthService.requestToken
            subscriber.onNext(oAuthService.getAuthorizationUrl(requestToken))
            subscriber.onCompleted()
        }
    }

    public fun requestUserInfo(pinCode: String): Observable<Account> {
        return Observable.create<Account>{ subscriber ->
            if (requestToken == null) {
                subscriber.onError(ApiRequestException("Request token is not exist."))
            }
            val accessToken = oAuthService.getAccessToken(requestToken, Verifier(pinCode))
            val request = OAuthRequest(Verb.GET, "http://n.hatena.com/applications/my.json")
            request.addHeader("Content-Type", "application/octed-stream")
            oAuthService.signRequest(accessToken, request)
            val response = request.send()
            try {
                val user = Account()
                user.token = accessToken.token
                user.tokenSecret = accessToken.secret
                val json = JSONObject(response.body)
                user.urlName = json.getString("url_name")
                user.displayName = json.getString("display_name")
                user.imageUrl = json.getString("profile_image_url")
                subscriber.onNext(user)
                subscriber.onCompleted()
            } catch (e: JSONException) {
                subscriber.onError(ApiRequestException("JSONException"))
            }

        }
    }
}
