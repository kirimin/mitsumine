package me.kirimin.mitsumine.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.kirimin.mitsumine.R;
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
        MAIN("", R.string.feed_main),
        SOCIAL("/social", R.string.feed_social),
        ECONOMICS("/economics", R.string.feed_economics),
        LIFE("/life", R.string.feed_life),
        KNOWLEDGE("/knowledge", R.string.feed_knowledge),
        IT("/it", R.string.feed_it),
        FUN("/fun", R.string.feed_fun),
        ENTERTAINMENT("/entertainment", R.string.feed_entertainment),
        GAME("/game", R.string.feed_game);

        private final String url;
        private final int labelResource;

        private CATEGORY(String url, int labelResource) {
            this.url = url;
            this.labelResource = labelResource;
        }

        @Override
        public String toString() {
            return url;
        }

        public int getLabelResource() {
            return labelResource;
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
