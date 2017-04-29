package me.kirimin.mitsumine._common.network.entity;

data class EntryInfoResponse(
        val title: String? = null,
        val count: Int = 0,
        val url: String? = null,
        val screenshot: String? = null,
        val bookmarks: List<BookmarkResponse> = emptyList(),
        val eid: String? = null)