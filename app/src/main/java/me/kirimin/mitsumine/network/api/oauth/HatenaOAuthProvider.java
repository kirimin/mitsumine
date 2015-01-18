package me.kirimin.mitsumine.network.api.oauth;

import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.utils.OAuthEncoder;

public class HatenaOAuthProvider extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.hatena.com/oauth/token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        String scope = "?scope=read_public%2Cwrite_public%2Cread_private%2Cwrite_private";
        return "https://www.hatena.com/oauth/initiate" + scope;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format("https://www.hatena.ne.jp/oauth/authorize?oauth_token=%s",
                OAuthEncoder.encode(requestToken.getToken()));
    }
}
