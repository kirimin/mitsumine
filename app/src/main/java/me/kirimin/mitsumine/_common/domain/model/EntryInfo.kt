package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse

data class EntryInfo(val response: EntryInfoResponse) {

    val title by lazy { response.title ?: "empty" }

    val url by lazy { response.url ?: "" }

    val bookmarkCount by lazy { response.count }

    val thumbnailUrl by lazy { response.screenshot ?: "" }

    val entryId by lazy { response.eid ?: "" }

    val bookmarkList by lazy {
        response.bookmarks.map { Bookmark(it) }
    }

    val tagList by lazy {
        response.bookmarks
                .flatMap { it.tags }
                .groupBy { it }
                .map { it.value }
                .sortedByDescending { it.size }
                .take(4)
                .map { it[0] }
    }

    val isNullObject by lazy { response.title == "empty" }

    val tagListString by lazy { tagList.joinToString(", ") }

}