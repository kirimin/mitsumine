package me.kirimin.mitsumine.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import me.kirimin.mitsumine.model.MyBookmark
import rx.Observable
import toList

public class MyBookmarksFunc {
    companion object {

        public fun toObservable(response: JSONObject): Observable<MyBookmark> {
            try {
                val myBookmarks = response.getJSONArray("bookmarks").toList<JSONObject>().map { bookmark ->
                    val comment = bookmark.getString("comment")
                    val entryObject = bookmark.getJSONObject("entry")
                    val title = entryObject.getString("title")
                    val count = entryObject.getInt("count")
                    val url = entryObject.getString("url")
                    MyBookmark(title, comment, count, url)
                }
                return Observable.from(myBookmarks)
            } catch (e: JSONException) {
                return Observable.empty<MyBookmark>()
            }
        }
    }
}
