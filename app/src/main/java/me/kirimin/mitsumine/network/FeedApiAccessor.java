package me.kirimin.mitsumine.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.kirimin.mitsumine.model.Feed;
import rx.Observable;
import rx.Subscriber;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

public class FeedApiAccessor extends ApiAccessor {

    public enum TYPE {
        HOT("hotentry"), NEW("entrylist");
        private final String TEXT;

        private TYPE(String type) {
            TEXT = type;
        }

        @Override
        public String toString() {
            return TEXT;
        }
    }

    public enum CATEGORY {
        MAIN(""), SOCIAL("/social"), ECONOMICS("/economics"), LIFE("/life"), KNOWLEDGE("/knowledge"),
        IT("/it"), FUN("/fun"), ENTERTAINMENT("/entertainment"), GAME("/game");
        private final String TEXT;

        private CATEGORY(String category) {
            TEXT = category;
        }

        @Override
        public String toString() {
            return TEXT;
        }
    }

    private static final String FEED_URL_HEADER = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://b.hatena.ne.jp/";
    private static final String FEED_URL_FOOTER = ".rss&num=-1";

    public static Observable<JSONObject> requestCategory(RequestQueue requestQueue, CATEGORY category, TYPE type) {
        return apiRequest(requestQueue, FEED_URL_HEADER + type + category + FEED_URL_FOOTER);
    }

    public static Observable<JSONObject> requestUserBookmark(RequestQueue requestQueue, String userName) {
        return apiRequest(requestQueue, FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER);
    }

    public static Observable<JSONObject> requestKeyword(RequestQueue requestQueue, String keyword) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return apiRequest(requestQueue, FEED_URL_HEADER + "keyword/" + keyword + "?mode=rss&num=-1");
    }
}
