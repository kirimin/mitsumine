package me.kirimin.mitsumine.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.MyBookmark;
import rx.Observable;
import rx.functions.Func1;

public class MyBookmarksFunc {

    public static Func1<JSONObject, Observable<MyBookmark>> mapToMyBookmarkList() {
        return new Func1<JSONObject, Observable<MyBookmark>>() {
            @Override
            public Observable<MyBookmark> call(JSONObject response) {
                List<MyBookmark> myBookmarks = new ArrayList<>();
                try {
                    JSONArray array = response.getJSONArray("bookmarks");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject bookmarkObject = array.getJSONObject(i);
                        String comment = bookmarkObject.getString("comment");

                        JSONObject entryObject = bookmarkObject.getJSONObject("entry");
                        String title = entryObject.getString("title");
                        int count = entryObject.getInt("count");
                        String url = entryObject.getString("url");
                        myBookmarks.add(new MyBookmark(title, comment, count, url));
                    }
                    return Observable.from(myBookmarks);
                } catch (JSONException e) {
                    return Observable.empty();
                }
            }
        };
    }
}
