package me.kirimin.mitsumine._common.domain.model

import android.text.Html
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import java.io.Serializable
import java.util.regex.Pattern

data class Bookmark(
        val response: BookmarkResponse,
        val stars: List<Star> = emptyList()) : Serializable {
    private val urlLinkPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE)

    val user: String
        get() = response.user

    val tags: List<String>
        get() = response.tags

    val timeStamp: String
        get() = response.timeStamp?.let { it.substring(0, it.indexOf(" ")) } ?: ""

    val comment: CharSequence
        get() = response.comment?.let { parseCommentToHtmlTag(it) } ?: ""

    val userIcon: String
        get() = "http://cdn1.www.st-hatena.com/users/" + user.subSequence(0, 2) + "/" + user + "/profile.gif"

    val hasComment = comment.toString() != ""

    var isPrivate = false

    private fun parseCommentToHtmlTag(comment: String): CharSequence {
        val matcher = urlLinkPattern.matcher(comment)
        return Html.fromHtml(matcher.replaceAll("<a href=\"$0\">$0</a>"))
    }
}