package me.kirimin.mitsumine.util

import android.text.Html

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.model.EntryInfo
import rx.functions.Func1

public class EntryInfoFunc {
    companion object {

        private val urlLinkPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE)

        public fun toEntryInfo(response: JSONObject): EntryInfo {
            try {
                val title = response.getString("title")
                val count = response.getInt("count")
                val url = response.getString("url")
                val thumbnail = response.getString("screenshot")
                val bookmarkList = parseBookmarkList(response.getJSONArray("bookmarks"))
                return EntryInfo(title, count, url, thumbnail, bookmarkList)
            } catch (e: JSONException) {
                return EntryInfo()
            }
        }

        public fun hasComment(bookmark: Bookmark): Boolean {
            return !bookmark.comment.toString().isEmpty()
        }

        public fun mapToMyBookmarkInfo(): Func1<JSONObject, Bookmark> {
            return object : Func1<JSONObject, Bookmark> {
                override fun call(jsonObject: JSONObject): Bookmark? {
                    try {
                        val user = jsonObject.getString("user")
                        val comment = jsonObject.getString("comment")
                        val timeStamp = jsonObject.getString("created_datetime")
                        val tagJsonArray = jsonObject.getJSONArray("tags")
                        val tags = ArrayList<String>()
                        for (i in 0..tagJsonArray.length() - 1) {
                            tags.add(tagJsonArray.getString(i))
                        }
                        val bookmark = Bookmark(user, tags, timeStamp, comment, "")
                        bookmark.setPrivate(jsonObject.getBoolean("private"))
                        return bookmark
                    } catch (e: JSONException) {
                        return null
                    }

                }
            }
        }

        throws(javaClass<JSONException>())
        private fun parseBookmarkList(bookmarks: JSONArray): List<Bookmark> {
            val bookmarkList = ArrayList<Bookmark>()
            for (i in 0..bookmarks.length() - 1) {
                val bookmark = bookmarks.getJSONObject(i)
                val user = bookmark.getString("user")
                val comment = parseCommentToHtmlTag(bookmark.getString("comment"))
                val timeStampTmp = bookmark.getString("timestamp")
                val timeStamp = timeStampTmp.substring(0, timeStampTmp.indexOf(" "))
                val userIcon = "http://cdn1.www.st-hatena.com/users/" + user.subSequence(0, 2) + "/" + user + "/profile.gif"
                val tags = parseTags(bookmark.getJSONArray("tags"))
                bookmarkList.add(Bookmark(user, tags, timeStamp, comment, userIcon))
            }
            return bookmarkList
        }

        throws(javaClass<JSONException>())
        private fun parseTags(tags: JSONArray): List<String> {
            val results = ArrayList<String>()
            for (i in 0..tags.length() - 1) {
                results.add(tags.getString(i))
            }
            return results
        }

        private fun parseCommentToHtmlTag(comment: String): CharSequence {
            val matcher = urlLinkPattern.matcher(comment)
            return Html.fromHtml(matcher.replaceAll("<a href=\"$0\">$0</a>"))
        }
    }
}
