package me.kirimin.mitsumine.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;

public abstract class ApiAccessor {

    protected static Observable<JSONObject> apiRequest(final RequestQueue requestQueue, final String url) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                requestQueue.add(new JsonObjectRequest(url, null, future, future));
                try {
                    JSONObject response = future.get();
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (InterruptedException | ExecutionException e) {
                    subscriber.onError(new ApiRequestException(e.getMessage()));
                }
            }
        });
    }
}