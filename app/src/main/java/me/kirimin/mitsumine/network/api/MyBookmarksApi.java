package me.kirimin.mitsumine.network.api;

import org.json.JSONObject;

import me.kirimin.mitsumine.model.Account;
import rx.Observable;
import rx.Subscriber;

public class MyBookmarksApi {

    public static Observable<JSONObject> request(final String url, final Account account) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                
            }
        });
    }
}
