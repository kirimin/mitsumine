package me.kirimin.mitsumine.network.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.List;

import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.network.ApiRequestException;
import me.kirimin.mitsumine.network.api.oauth.Consumer;
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthApi;
import rx.Observable;
import rx.Subscriber;

public class BookmarkApiAccessor {

    public static Observable<JSONObject> requestAddBookmark(final String url, final Account account, final String comment, final List<String> tags, final boolean isPrivate) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                OAuthService oAuthService = new ServiceBuilder()
                        .provider(HatenaOAuthApi.class)
                        .apiKey(Consumer.K)
                        .apiSecret(Consumer.S)
                        .build();

                Token accessToken = new Token(account.token, account.tokenSecret);

                OAuthRequest request = new OAuthRequest(Verb.POST, "http://api.b.hatena.ne.jp/1/my/bookmark");
                request.addQuerystringParameter("url", url);
                request.addQuerystringParameter("comment", comment);
                for (String tag : tags) {
                    request.addQuerystringParameter("tags", tag);
                }
                if (isPrivate) {
                    request.addQuerystringParameter("private", "true");
                }
                oAuthService.signRequest(accessToken, request);
                Response response = request.send();
                if (response.getCode() != 200) {
                    subscriber.onError(new ApiRequestException("error code:" + response.getCode()));
                    return;
                }
                try {
                    subscriber.onNext(new JSONObject(response.getBody()));
                    subscriber.onCompleted();
                } catch (JSONException e) {
                    subscriber.onError(new ApiRequestException(e.getMessage()));
                }
            }
        });
    }

    public static Observable<Boolean> requestDeleteBookmark(final String url, final Account account) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                OAuthService oAuthService = new ServiceBuilder()
                        .provider(HatenaOAuthApi.class)
                        .apiKey(Consumer.K)
                        .apiSecret(Consumer.S)
                        .build();

                Token accessToken = new Token(account.token, account.tokenSecret);

                OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://api.b.hatena.ne.jp/1/my/bookmark");
                request.addQuerystringParameter("url", url);
                oAuthService.signRequest(accessToken, request);
                Response response = request.send();
                if (response.getCode() != 204) {
                    subscriber.onError(new ApiRequestException("error code:" + response.getCode()));
                    return;
                }
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    public static Observable<JSONObject> requestBookmarkInfo(final String url, final Account account) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                OAuthService oAuthService = new ServiceBuilder()
                        .provider(HatenaOAuthApi.class)
                        .apiKey(Consumer.K)
                        .apiSecret(Consumer.S)
                        .build();

                Token accessToken = new Token(account.token, account.tokenSecret);

                OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.b.hatena.ne.jp/1/my/bookmark");
                request.addQuerystringParameter("url", url);
                oAuthService.signRequest(accessToken, request);
                Response response = request.send();
                if (response.getCode() == 200) {
                    try {
                        subscriber.onNext(new JSONObject(response.getBody()));
                        subscriber.onCompleted();
                    } catch (JSONException e) {
                        subscriber.onError(new ApiRequestException(""));
                    }
                } else if (response.getCode() == 404) {
                    subscriber.onNext(new JSONObject());
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new ApiRequestException(""));
                }
            }
        });
    }
}