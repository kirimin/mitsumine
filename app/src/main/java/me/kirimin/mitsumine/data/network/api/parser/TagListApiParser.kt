package me.kirimin.mitsumine.data.network.api.parser

import me.kirimin.mitsumine.domain.common.util.toList
import org.json.JSONException
import org.json.JSONObject

public class TagListApiParser {
    companion object {

        public fun parseResponse(response: JSONObject): List<String> {
            try {
                val tags = response.getJSONArray("bookmarks").toList<JSONObject>()
                        .map { bookmark -> bookmark.getJSONArray("tags").toList<kotlin.String>() }
                        .flatMap { tags -> tags }
                        .groupBy { tags -> tags }
                        .map { tagMap -> tagMap.getValue() }
                        .sortedByDescending { tags -> tags.size() }
                        .take(4)
                        .map { tags -> tags.get(0) }
                return tags
            } catch (e: JSONException) {
                return emptyList()
            }
        }
    }
}
