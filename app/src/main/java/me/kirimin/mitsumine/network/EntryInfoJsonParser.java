package me.kirimin.mitsumine.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;

public class EntryInfoJsonParser {

    public static EntryInfo parseResponse(JSONObject response) throws JSONException {
        String title = response.getString("title");
        int count = response.getInt("count");
        String url = response.getString("url");
        String thumbnail = response.getString("screenshot");
        List<Bookmark> bookmarkList = parseBookmarkList(response.getJSONArray("bookmarks"));
        return new EntryInfo(title, count, url, thumbnail, bookmarkList);
    }

    private static List<Bookmark> parseBookmarkList(JSONArray bookmarks) throws JSONException {
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (int i = 0; i < bookmarks.length(); i++) {
            JSONObject bookmark = bookmarks.getJSONObject(i);
            String user = bookmark.getString("user");
            String comment = bookmark.getString("comment");
            String timeStamp = bookmark.getString("timestamp");
            String userIcon = "http://n.hatena.com/" + user + "/profile/image.gif?type=face&size=64";
            List<String> tags = parseTags(bookmark.getJSONArray("tags"));
            bookmarkList.add(new Bookmark(user, tags, timeStamp, comment, userIcon));
        }
        return bookmarkList;
    }

    private static List<String> parseTags(JSONArray tags) throws JSONException {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < tags.length(); i++) {
            results.add(tags.getString(i));
        }
        return results;
    }
}
