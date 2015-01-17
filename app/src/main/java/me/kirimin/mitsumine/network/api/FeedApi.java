package me.kirimin.mitsumine.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Observable;

import org.json.JSONObject;

import com.android.volley.RequestQueue;

public class FeedApi {

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
        return ApiAccessor.apiRequest(requestQueue, FEED_URL_HEADER + type + category + FEED_URL_FOOTER);
    }

    public static Observable<JSONObject> requestUserBookmark(RequestQueue requestQueue, String userName) {
        return ApiAccessor.apiRequest(requestQueue, FEED_URL_HEADER + userName + "/bookmark" + FEED_URL_FOOTER);
    }

    public static Observable<JSONObject> requestKeyword(RequestQueue requestQueue, String keyword) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return ApiAccessor.apiRequest(requestQueue, FEED_URL_HEADER + "keyword/" + keyword + "?mode=rss&num=-1");
    }
}
