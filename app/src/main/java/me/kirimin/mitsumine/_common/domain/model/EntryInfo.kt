package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse

data class EntryInfo(val response: EntryInfoResponse) {

    val title
        get() = response.title ?: "empty"

    val url
        get() = response.url ?: ""

    val bookmarkCount
        get() = response.count

    val thumbnailUrl
        get() = response.screenshot ?: ""

    val entryId
        get() = response.eid ?: ""

    val bookmarkList
        get() = response.bookmarks.map { Bookmark(it) }

    val tagList
        get() = response.bookmarks
                .flatMap { it.tags }
                .groupBy { it }
                .map { it.value }
                .sortedByDescending { it.size }
                .take(4)
                .map { it[0] }

    val isNullObject
        get() = response.title == "empty"

    val tagListString
        get() = tagList.joinToString(", ")
}