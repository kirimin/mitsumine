package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.MyBookmarksResponse

class MyBookmark(private val response: MyBookmarksResponse.MyBookmarkResponse) {

    val title get() = response.entry.title
    val comment get() = response.comment
    val bookmarkCount get() = response.entry.count
    val linkUrl get() = response.entry.url
    val snippet: String get() = response.entry.snippet
}