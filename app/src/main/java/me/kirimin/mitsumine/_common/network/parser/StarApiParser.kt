package me.kirimin.mitsumine._common.network.parser

import me.kirimin.mitsumine._common.domain.extensions.toList
import me.kirimin.mitsumine._common.domain.model.Star
import org.json.JSONObject

object StarApiParser {

    fun parseResponse(json: JSONObject): List<Star> {
        val stars = when (json.getJSONArray("entries").getJSONObject(0).has("stars")) {
            true -> json.getJSONArray("entries").getJSONObject(0).getJSONArray("stars").toList<JSONObject>().map {
                Star(it.getString("name"))
            }
            else -> emptyList()
        }
        val coloredStars = when (json.getJSONArray("entries").getJSONObject(0).has("colored_stars")) {
            true -> json.getJSONArray("entries").getJSONObject(0).getJSONArray("colored_stars").toList<JSONObject>().map {
                it.getJSONArray("stars").toList<JSONObject>().map { Star(it.getString("name")) }
            }
            else -> emptyList()
        }.flatMap { it }
        return stars.plus(coloredStars)
    }
}