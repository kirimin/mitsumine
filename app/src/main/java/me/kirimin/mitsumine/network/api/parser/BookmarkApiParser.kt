package me.kirimin.mitsumine.network.api.parser

import me.kirimin.mitsumine.model.Bookmark
import org.json.JSONException
import org.json.JSONObject
import toList

public class BookmarkApiParser {
    companion object {

        public fun mapToMyBookmarkInfo(response: JSONObject): Bookmark? {
            return try {
                val user = response.getString("user")
                val comment = response.getString("comment")
                val timeStamp = response.getString("created_datetime")
                val tags = response.getJSONArray("tags").toList<String>()
                val bookmark = Bookmark(user, tags, timeStamp, comment, "")
                bookmark.setPrivate(response.getBoolean("private"))
                bookmark
            } catch (e: JSONException) {
                null
            }
        }
    }
}