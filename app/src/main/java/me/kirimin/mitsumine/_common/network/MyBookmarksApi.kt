//package me.kirimin.mitsumine._common.network
//
//import me.kirimin.mitsumine.BuildConfig
//import org.json.JSONException
//import org.json.JSONObject
//import org.scribe.builder.ServiceBuilder
//import org.scribe.model.OAuthRequest
//import org.scribe.model.Token
//import org.scribe.model.Verb
//
//import me.kirimin.mitsumine._common.domain.model.Account
//import me.kirimin.mitsumine._common.domain.model.MyBookmark
//import me.kirimin.mitsumine._common.network.oauth.HatenaOAuthProvider
//import me.kirimin.mitsumine._common.network.parser.MyBookmarksApiParser
//import rx.Observable
//
//object MyBookmarksApi {
//
//    fun request(account: Account, keyword: String, offset: Int): Observable<MyBookmark> {
//        return Observable.create<JSONObject> { subscriber ->
//            val oAuthService = ServiceBuilder().provider(HatenaOAuthProvider::class.java).apiKey(BuildConfig.OAUTH_KEY).apiSecret(BuildConfig.OAUTH_SECRET).build()
//            val accessToken = Token(account.token, account.tokenSecret)
//            val request = OAuthRequest(Verb.GET, "http://b.hatena.ne.jp/" + account.urlName + "/search/json")
//            if (!keyword.isEmpty()) {
//                request.addQuerystringParameter("q", keyword)
//            }
//            request.addQuerystringParameter("of", offset.toString())
//            request.addQuerystringParameter("limit", "50")
//            oAuthService.signRequest(accessToken, request)
//            val response = request.send()
//            val body = response.getBody()
//            try {
//                subscriber.onNext(JSONObject(body))
//                subscriber.onCompleted()
//            } catch (e: JSONException) {
//                subscriber.onError(e)
//            }
//        }.flatMap { response -> MyBookmarksApiParser.parseResponse(response) }
//    }
//}
