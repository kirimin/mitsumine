package me.kirimin.mitsumine.network.api.parser

import org.json.JSONException
import org.json.JSONObject
import toList

public class TagListApiParser {
    companion object {

        public fun parseResponse(response: JSONObject): List<String> {
            try {
                val tags = response.getJSONArray("bookmarks").toList<JSONObject>()
                        .map { bookmark -> bookmark.getJSONArray("tags").toList<kotlin.String>() }
                        .flatMap { tags -> tags }
                        .groupBy { tags -> tags }
                        .map { tagMap -> tagMap.getValue() }
                        .sortDescendingBy { tags -> tags.size() }
                        .take(4)
                        .map { tags -> tags.get(0) }
                return tags
            } catch (e: JSONException) {
                return emptyList()
            }
        }
    }
}
