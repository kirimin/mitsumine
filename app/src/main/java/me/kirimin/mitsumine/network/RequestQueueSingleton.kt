package me.kirimin.mitsumine.network

import android.content.Context

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

public class RequestQueueSingleton private() {
    companion object {

        private var queue: RequestQueue? = null

        public fun get(context: Context): RequestQueue {
            if (queue == null) {
                queue = Volley.newRequestQueue(context)
            }
            return queue!!
        }
    }
}
