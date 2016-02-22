package me.kirimin.mitsumine.common.network.parser

import org.json.JSONException
import org.json.JSONObject

import me.kirimin.mitsumine.common.domain.model.MyBookmark
import me.kirimin.mitsumine.common.domain.extensions.toList
import rx.Observable

object MyBookmarksApiParser {

    fun parseResponse(response: JSONObject): Observable<MyBookmark> {
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