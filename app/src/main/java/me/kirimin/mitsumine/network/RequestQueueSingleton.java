package me.kirimin.mitsumine.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {

    private static RequestQueue queue;

    private RequestQueueSingleton() {
    }

    public static RequestQueue getRequestQueue(final Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }
}
