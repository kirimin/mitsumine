package me.kirimin.mitsumine.network;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookmarkFeedJsonParser {

    public static List<Feed> parseResponse(JSONObject response) throws JSONException {
        JSONArray entries;
        entries = response.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries");
        List<Feed> feedList = new ArrayList<>();
        for (int i = 0; i < entries.length(); i++) {
            feedList.add(parseFeed(entries.getJSONObject(i)));
        }
        return feedList;
    }

    private static Feed parseFeed(JSONObject entriesObject) throws JSONException {
        Feed feed = new Feed();
        feed.title = entriesObject.getString("title");
        feed.linkUrl = entriesObject.getString("link");
        feed.content = entriesObject.getString("contentSnippet");
        String content = entriesObject.getString("content");
        feed.thumbnailUrl = parseThumbnailUrl(content);
        feed.bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + feed.linkUrl;
        feed.faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + feed.linkUrl;
        feed.entryLinkUrl = "http://b.hatena.ne.jp/entry/" + feed.linkUrl;
        return feed;
    }

    private static String parseThumbnailUrl(String content) {
        int urlStartIndex = content.indexOf("http://cdn-ak.b.st-hatena.com/entryimage/");
        if (urlStartIndex != -1) {
            return content.substring(urlStartIndex, content.indexOf("\"", urlStartIndex));
        } else {
            return "";
        }
    }

    private BookmarkFeedJsonParser() {
    }
}
