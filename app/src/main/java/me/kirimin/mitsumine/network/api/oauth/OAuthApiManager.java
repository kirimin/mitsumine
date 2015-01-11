package me.kirimin.mitsumine.network.api.oauth;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.network.ApiRequestException;
import rx.Observable;
import rx.Subscriber;

public class OAuthApiManager {

    private final OAuthService oAuthService;
    private Token requestToken;

    public OAuthApiManager() {
        oAuthService = new ServiceBuilder()
                .provider(HatenaOAuthApi.class)
                .apiKey(Consumer.K)
                .apiSecret(Consumer.S)
                .build();
    }

    public Observable<String> requestAuthUrl() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                requestToken = oAuthService.getRequestToken();
                subscriber.onNext(oAuthService.getAuthorizationUrl(requestToken));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Account> requestUserInfo(final String pinCode) {
        return Observable.create(new Observable.OnSubscribe<Account>() {
            @Override
            public void call(Subscriber<? super Account> subscriber) {
                if (requestToken == null) {
                    subscriber.onError(new ApiRequestException("Request token is not exist."));
                }
                Token accessToken = oAuthService.getAccessToken(requestToken, new Verifier(pinCode));
                OAuthRequest request = new OAuthRequest(Verb.GET, "http://n.hatena.com/applications/my.json");
                request.addHeader("Content-Type", "application/octed-stream");
                oAuthService.signRequest(accessToken, request);
                Response response = request.send();
                try {
                    Account user = new Account();
                    user.token = accessToken.getToken();
                    user.tokenSecret = accessToken.getSecret();
                    JSONObject json = new JSONObject(response.getBody());
                    user.urlName = json.getString("url_name");
                    user.displayName = json.getString("display_name");
                    user.imageUrl = json.getString("profile_image_url");
                    subscriber.onNext(user);
                    subscriber.onCompleted();
                } catch (JSONException e) {
                    subscriber.onError(new ApiRequestException(e.getMessage()));
                }
            }
        });
    }
}
