package me.kirimin.mitsumine._common.network.entity

import java.io.Serializable

data class BookmarkResponse(
        var user: String = "__",
        var tags: List<String> = emptyList(),
        var timeStamp: String? = null,
        var comment: String? = null) : Serializable {
}