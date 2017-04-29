package me.kirimin.mitsumine._common.network.entity

import com.google.gson.annotations.SerializedName

data class MyBookmarksResponse(val bookmarks: List<MyBookmarkResponse>, val meta: MetaResponse) {

    data class MyBookmarkResponse(
            val entry: EntryResponse,
            val timestamp: String,
            val comment: String,
            @SerializedName("is_private")
            val isPrivate: Int)

    data class EntryResponse(
            val title: String,
            val count: Int,
            val url: String,
            val eid: String,
            val snippet: String)

    data class MetaResponse(val total: Int)
}