package me.kirimin.mitsumine.network.api.parser

import org.json.JSONException
import org.json.JSONObject

import me.kirimin.mitsumine.model.MyBookmark
import me.kirimin.mitsumine.util.toList
import rx.Observable

public class MyBookmarksApiParser {
    companion object {

        public fun parseResponse(response: JSONObject): Observable<MyBookmark> {
            try {
                val total = response.getJSONObject("meta").getInt("total")
                val myBookmarks = response.getJSONArray("bookmarks").toList<JSONObject>().map { bookmark ->
                    val comment = bookmark.getString("comment")
                    val entryObject = bookmark.getJSONObject("entry")
                    val title = entryObject.getString("title")
                    val count = entryObject.getInt("count")
                    val url = entryObject.getString("url")
                    MyBookmark(title, comment, count, url, total)
                }
                return Observable.from(myBookmarks)
            } catch (e: JSONException) {
                return Observable.empty<MyBookmark>()
            }
        }
    }
}
