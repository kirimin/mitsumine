package me.kirimin.mitsumine._common.domain.model

class EntryInfo(val title: String = "empty",
                val bookmarkCount: Int = 0,
                val url: String = "",
                val thumbnailUrl: String = "",
                val bookmarkList: List<Bookmark> = emptyList(),
                val entryId: String = "") {

    val tagList: List<String> = bookmarkList
            .flatMap { it.tags }
            .groupBy { it }
            .map { it.value }
            .sortedByDescending { it.size }
            .take(4)
            .map { it[0] }

    fun isNullObject() = title == "empty"

    fun tagListString() = tagList.joinToString(", ")
}
