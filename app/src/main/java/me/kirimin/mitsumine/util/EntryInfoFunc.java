package me.kirimin.mitsumine.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.model.EntryInfo;
import rx.functions.Func1;

public class EntryInfoFunc {

    public static Func1<JSONObject, EntryInfo> mapToEntryInfo() {
        return new Func1<JSONObject, EntryInfo>() {
            @Override
            public EntryInfo call(JSONObject response) {
                try {
                    String title = response.getString("title");
                    int count = response.getInt("count");
                    String url = response.getString("url");
                    String thumbnail = response.getString("screenshot");
                    List<Bookmark> bookmarkList = parseBookmarkList(response.getJSONArray("bookmarks"));
                    return new EntryInfo(title, count, url, thumbnail, bookmarkList);
                } catch (JSONException e) {
                    return null;
                }
            }
        };
    }

    public static Func1<EntryInfo, Boolean> isNotNullEntryInfo() {
        return new Func1<EntryInfo, Boolean>() {
            @Override
            public Boolean call(EntryInfo entryInfo) {
                return entryInfo != null;
            }
        };
    }

    public static Func1<Bookmark, Boolean> hasComment() {
        return new Func1<Bookmark, Boolean>() {
            @Override
            public Boolean call(Bookmark bookmark) {
                return !bookmark.getComment().equals("");
            }
        };
    }

    public static Func1<JSONObject, Bookmark> mapToMyBookmarkInfo() {
        return new Func1<JSONObject, Bookmark>() {
            @Override
            public Bookmark call(JSONObject jsonObject) {
                try {
                    String user = jsonObject.getString("user");
                    String comment = jsonObject.getString("comment");
                    String timeStamp = jsonObject.getString("created_datetime");
                    JSONArray tagJsonArray = jsonObject.getJSONArray("tags");
                    List<String> tags = new ArrayList<>();
                    for (int i = 0; i < tagJsonArray.length(); i++) {
                        tags.add(tagJsonArray.getString(i));
                    }
                    return new Bookmark(user, tags, timeStamp, comment, "");
                } catch (JSONException e) {
                    return null;
                }
            }
        };
    }

    private static List<Bookmark> parseBookmarkList(JSONArray bookmarks) throws JSONException {
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (int i = 0; i < bookmarks.length(); i++) {
            JSONObject bookmark = bookmarks.getJSONObject(i);
            String user = bookmark.getString("user");
            String comment = bookmark.getString("comment");
            String timeStamp = bookmark.getString("timestamp");
            timeStamp = timeStamp.substring(0, timeStamp.indexOf(" "));
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
