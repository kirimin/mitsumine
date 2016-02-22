package me.kirimin.mitsumine.common.domain.extensions

import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

fun <T> JSONArray.toList(): List<T> {
    try {
        val newArray = ArrayList<T>()
        for (i in 0..length() - 1) {
            newArray.add(get(i) as T)
        }
        return newArray
    } catch (e: Exception) {
        throw JSONException("parse error")
    }
}