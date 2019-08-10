package me.kirimin.mitsumine._common.network.oauth

import org.scribe.builder.api.*
import org.scribe.model.*
import org.scribe.utils.OAuthEncoder

class HatenaOAuthProvider : DefaultApi10a() {

    override fun getAccessTokenEndpoint(): String {
        return "https://www.hatena.com/oauth/token"
    }

    override fun getRequestTokenEndpoint(): String {
        val scope = "?scope=read_public%2Cwrite_public%2Cread_private%2Cwrite_private"
        return "https://www.hatena.com/oauth/initiate$scope"
    }

    override fun getAuthorizationUrl(requestToken: Token): String {
        return java.lang.String.format("https://www.hatena.ne.jp/oauth/authorize?oauth_token=%s", OAuthEncoder.encode(requestToken.token))
    }
}
