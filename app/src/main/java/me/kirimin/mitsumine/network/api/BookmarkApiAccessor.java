package me.kirimin.mitsumine.network.api;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.network.ApiRequestException;
import me.kirimin.mitsumine.network.api.oauth.Consumer;
import me.kirimin.mitsumine.network.api.oauth.HatenaOAuthApi;
import rx.Observable;
import rx.Subscriber;

public class BookmarkApiAccessor {

    public static Observable<Boolean> requestAddBookmark(final String url, final Account account) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                OAuthService oAuthService = new ServiceBuilder()
                        .provider(HatenaOAuthApi.class)
                        .apiKey(Consumer.K)
                        .apiSecret(Consumer.S)
                        .build();

                Token accessToken = new Token(account.token, account.tokenSecret);

                String xml = String.format("<entry xmlns='http://purl.org/atom/ns#'>"
                        + "<title>dummy</title>"
                        + "<link rel='related' type='text/html' href='%s' />"
                        + "<summary type='text/plain'></summary>"
                        + "</entry>", url);

                OAuthRequest request = new OAuthRequest(Verb.POST, "http://b.hatena.ne.jp/atom/post");
                request.addHeader("Content-Type", "application/octed-stream");
                oAuthService.signRequest(accessToken, request);
                request.addPayload(xml);
                Response response = request.send();
                if (response.getCode() == 201) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new ApiRequestException("error code:" + response.getCode()));
                }
            }
        });
    }
}