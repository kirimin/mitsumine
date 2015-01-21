package me.kirimin.mitsumine.network.api;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import me.kirimin.mitsumine.network.ApiRequestException;
import rx.Observable;
import rx.Subscriber;

class ApiAccessor {

    static Observable<JSONObject> apiRequest(final RequestQueue requestQueue, final String url) {
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

    static Observable<String> stringRequest(final RequestQueue requestQueue, final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                RequestFuture<String> future = RequestFuture.newFuture();
                requestQueue.add(new StringRequest(url, future, future));
                try {
                    String response = future.get();
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (InterruptedException | ExecutionException e) {
                    subscriber.onError(new ApiRequestException(e.getMessage()));
                }
            }
        });
    }
}