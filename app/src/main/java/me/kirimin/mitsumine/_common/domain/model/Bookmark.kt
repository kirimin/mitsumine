package me.kirimin.mitsumine._common.domain.model

import android.text.Html
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import java.io.Serializable
import java.util.regex.Pattern

open class Bookmark(private val response: BookmarkResponse) : Serializable {

    var stars: Stars? = null

    val user: String
        get() = response.user

    val tags: List<String>
        get() = response.tags

    val timestamp: String
        get() = response.timestamp?.let { it.substring(0, it.indexOf(" ")) } ?: ""

    val comment: CharSequence
        get() = response.comment?.let { parseCommentToHtmlTag(it) } ?: ""

    val userIcon: String
        get() = "http://cdn1.www.st-hatena.com/users/" + user.subSequence(0, 2) + "/" + user + "/profile.gif"

    val hasComment = comment.toString() != ""

    val isPrivate
        get() = response.private

    private fun parseCommentToHtmlTag(comment: String): CharSequence {
        val matcher = urlLinkPattern.matcher(comment)
        return Html.fromHtml(matcher.replaceAll("<a href=\"$0\">$0</a>"))
    }

    class EmptyBookmark : Bookmark(BookmarkResponse())

    companion object {
        private val urlLinkPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE)
    }
}