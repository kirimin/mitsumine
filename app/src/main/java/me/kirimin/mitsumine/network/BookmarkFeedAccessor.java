package me.kirimin.mitsumine.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import me.kirimin.mitsumine.model.Feed;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class BookmarkFeedAccessor {

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

    ;

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

    public interface FeedListener {
        void onSuccess(List<Feed> feedList);

        void onError(String errorMessage);
    }

    public static void requestCategory(RequestQueue requestQueue, final FeedListener listener, CATEGORY category, TYPE type) {
        request(requestQueue, listener, FEED_URL_HEADER + type + category + FEED_URL_FOOTER);
    }

    public static void requestUserBookmark(RequestQueue requestQueue, final FeedListener listener, String userName) {
        request(requestQueue, listener, FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER);
    }

    public static void requestKeyword(RequestQueue requestQueue, final FeedListener listener, String keyword) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        request(requestQueue, listener, FEED_URL_HEADER + "keyword/" + keyword + "?mode=rss&num=-1");
    }

    public static void request(RequestQueue requestQueue, final FeedListener listener, String url) {
        requestQueue.add(new JsonObjectRequest(Method.GET, url, null,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onSuccess(BookmarkFeedJsonParser.parseResponse(response));
                        } catch (JSONException e) {
                            listener.onError("json parse error");
                        }
                    }
                },
                new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.getMessage());
                    }
                }));
    }
}
