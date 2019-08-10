package me.kirimin.mitsumine._common.network.oauth

import me.kirimin.mitsumine.BuildConfig
import org.json.JSONException
import org.json.JSONObject
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService

import me.kirimin.mitsumine._common.domain.model.Account
import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import rx.Observable

class HatenaOAuth {

    private val oAuthService: OAuthService
    private var requestToken: Token? = null

    init {
        oAuthService = ServiceBuilder().provider(HatenaOAuthProvider::class.java).apiKey(BuildConfig.OAUTH_KEY).apiSecret(BuildConfig.OAUTH_SECRET).build()
    }

    fun requestAuthUrl(): Observable<String> {
        return Observable.create { subscriber ->
            requestToken = oAuthService.requestToken
            subscriber.onNext(oAuthService.getAuthorizationUrl(requestToken))
            subscriber.onCompleted()
        }
    }

    fun requestUserInfo(pinCode: String): Observable<Account> {
        return Observable.create { subscriber ->
            if (requestToken == null) {
                subscriber.onError(ApiRequestException("Request token is not exist."))
            }
            val accessToken = oAuthService.getAccessToken(requestToken, Verifier(pinCode))
            val request = OAuthRequest(Verb.GET, "http://n.hatena.com/applications/my.json")
            request.addHeader("Content-Type", "application/octed-stream")
            oAuthService.signRequest(accessToken, request)
            val response = request.send()
            try {
                val json = JSONObject(response.body)
                val user = Account(token = accessToken.token,
                        tokenSecret = accessToken.secret,
                        urlName = json.getString("url_name"),
                        displayName = json.getString("display_name"),
                        imageUrl = json.getString("profile_image_url"))
                subscriber.onNext(user)
                subscriber.onCompleted()
            } catch (e: JSONException) {
                subscriber.onError(ApiRequestException("JSONException"))
            }

        }
    }
}
