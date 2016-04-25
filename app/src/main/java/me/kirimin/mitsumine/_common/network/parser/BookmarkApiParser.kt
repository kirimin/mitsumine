package me.kirimin.mitsumine._common.network.parser

import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.domain.extensions.toList
import org.json.JSONException
import org.json.JSONObject

object BookmarkApiParser {

    fun parseResponse(response: JSONObject): Bookmark? {
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