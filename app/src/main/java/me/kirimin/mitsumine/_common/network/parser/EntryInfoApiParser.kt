//package me.kirimin.mitsumine._common.network.parser
//
//import android.text.Html
//
//import org.json.JSONArray
//import org.json.JSONException
//import org.json.JSONObject
//
//import java.util.regex.Pattern
//
//import me.kirimin.mitsumine._common.domain.model.Bookmark
//import me.kirimin.mitsumine._common.domain.model.EntryInfo
//import me.kirimin.mitsumine._common.domain.extensions.toList
//import me.kirimin.mitsumine._common.network.StarApi
//
//object EntryInfoApiParser {
//
//    private val urlLinkPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE)
//
//    fun parseResponse(response: JSONObject): EntryInfo {
//        try {
//            val title = response.getString("title")
//            val count = response.getInt("count")
//            val url = response.getString("url")
//            val thumbnail = response.getString("screenshot")
//            val eid = response.getString("eid")
//            val bookmarkList = parseBookmarksObject(response.getJSONArray("bookmarks"))
//            return EntryInfo(title = title, bookmarkCount = count, url = url, thumbnailUrl = thumbnail, bookmarkList = bookmarkList, entryId = eid)
//        } catch (e: JSONException) {
//            return EntryInfo()
//        }
//    }
//
//    private fun parseBookmarksObject(bookmarks: JSONArray): List<Bookmark> {
//        return bookmarks.toList<JSONObject>().map { bookmark ->
//            val user = bookmark.getString("user")
//            val comment = parseCommentToHtmlTag(bookmark.getString("comment"))
//            val timeStampTmp = bookmark.getString("timestamp")
//            val timeStamp = timeStampTmp.substring(0, timeStampTmp.indexOf(" "))
//            val userIcon = "http://cdn1.www.st-hatena.com/users/" + user.subSequence(0, 2) + "/" + user + "/profile.gif"
//            val tags = bookmark.getJSONArray("tags").toList<String>()
//            Bookmark(user, tags, timeStamp, comment, userIcon, emptyList())
//        }
//    }
//
//    private fun parseCommentToHtmlTag(comment: String): CharSequence {
//        val matcher = urlLinkPattern.matcher(comment)
//        return Html.fromHtml(matcher.replaceAll("<a href=\"$0\">$0</a>"))
//    }
//}