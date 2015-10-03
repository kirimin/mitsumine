package me.kirimin.mitsumine.util
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

public fun JSONArray.forEach<T>(operation: (T) -> Unit): Unit {
    try {
        for (i in 0..length() - 1) {
            operation(get(i) as T)
        }
    } catch (e: Exception) {
        throw JSONException("parse error")
    }
}

public fun JSONArray.forEachIndexed<T>(operation: (T, Int) -> Unit): Unit {
    try {
        for (i in 0..length() - 1) {
            operation(get(i) as T, i)
        }
    } catch (e: Exception) {
        throw JSONException("parse error")
    }
}

public fun JSONArray.map<T, R>(transform: (T) -> R): JSONArray {
    try {
        val newArray = JSONArray()
        for (i in 0..length() - 1) {
            newArray.put(transform(get(i) as T))
        }
        return newArray
    } catch (e: Exception) {
        throw JSONException("parse error")
    }
}

public fun JSONArray.filter<T>(predicate: (T) -> Boolean): JSONArray {
    try {
        val newArray = JSONArray()
        for (i in 0..length() - 1) {
            if (!predicate(get(i) as T)) {
                newArray.put(get(i) as T)
            }
        }
        return newArray
    } catch (e: Exception) {
        throw JSONException("parse error")
    }
}

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