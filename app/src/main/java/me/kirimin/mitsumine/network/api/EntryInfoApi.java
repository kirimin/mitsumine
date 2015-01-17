package me.kirimin.mitsumine.network.api;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import rx.Observable;

public class EntryInfoApi extends ApiAccessor {

    private static final String REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url=";

    public static Observable<JSONObject> request(final RequestQueue requestQueue, final String url) {
        return apiRequest(requestQueue, REQUEST_URL + url);
    }
}