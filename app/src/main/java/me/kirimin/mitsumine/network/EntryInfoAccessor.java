package me.kirimin.mitsumine.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import me.kirimin.mitsumine.model.EntryInfo;

public class EntryInfoAccessor {

    public interface EntryInfoListener {
        void onSuccess(EntryInfo feedDetail);

        void onError(String errorMessage);
    }

    private static final String REQUEST_URL = "http://b.hatena.ne.jp/entry/jsonlite/?url=";

    public static void request(RequestQueue requestQueue, final EntryInfoListener listener, String url) {
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, REQUEST_URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onSuccess(EntryInfoJsonParser.parseResponse(response));
                        } catch (JSONException e) {
                            listener.onError("json parse error");
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.getMessage());
                    }
                }));
    }
}