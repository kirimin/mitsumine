package me.kirimin.mitsumine.network.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.network.api.oauth.Consumer;
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthProvider;
import rx.Observable;
import rx.Subscriber;

public class MyBookmarksApi {

    public static Observable<JSONObject> request(final Account account, final String keyword) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                OAuthService oAuthService = new ServiceBuilder()
                        .provider(HatenaOAuthProvider.class)
                        .apiKey(Consumer.K)
                        .apiSecret(Consumer.S)
                        .build();

                Token accessToken = new Token(account.token, account.tokenSecret);

                OAuthRequest request = new OAuthRequest(Verb.GET, "http://b.hatena.ne.jp/" + account.urlName + "/search/json");
                if (!keyword.isEmpty()) {
                    request.addQuerystringParameter("q", keyword);
                }
                oAuthService.signRequest(accessToken, request);
                Response response = request.send();
                String body = response.getBody();
                try {
                    subscriber.onNext(new JSONObject(body));
                    subscriber.onCompleted();
                } catch (JSONException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
