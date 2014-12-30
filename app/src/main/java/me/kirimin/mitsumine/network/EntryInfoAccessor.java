package me.kirimin.mitsumine.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;

public class EntryInfoAccessor extends ApiAccessor {

    private static final String REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url=";

    public static Observable<JSONObject> request(final RequestQueue requestQueue, final String url) {
        return apiRequest(requestQueue, REQUEST_URL + url);
    }
}