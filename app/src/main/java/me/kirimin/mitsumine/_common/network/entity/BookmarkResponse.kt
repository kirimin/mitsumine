package me.kirimin.mitsumine._common.network.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookmarkResponse(
        var user: String = "__",
        var tags: List<String> = emptyList(),
        var timestamp: String? = null,
        var private: Boolean = false,
        var comment: String? = null) : Serializable