package me.kirimin.mitsumine.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import me.kirimin.mitsumine.model.MyBookmark
import rx.Observable
import rx.functions.Func1

public class MyBookmarksFunc {
    companion object {

        public fun toObservable(response: JSONObject): Observable<MyBookmark> {
            val myBookmarks = ArrayList<MyBookmark>()
            try {
                val array = response.getJSONArray("bookmarks")
                for (i in 0..array.length() - 1) {
                    val bookmarkObject = array.getJSONObject(i)
                    val comment = bookmarkObject.getString("comment")

                    val entryObject = bookmarkObject.getJSONObject("entry")
                    val title = entryObject.getString("title")
                    val count = entryObject.getInt("count")
                    val url = entryObject.getString("url")
                    myBookmarks.add(MyBookmark(title, comment, count, url))
                }
                return Observable.from<MyBookmark>(myBookmarks)
            } catch (e: JSONException) {
                return Observable.empty<MyBookmark>()
            }
        }
    }
}
