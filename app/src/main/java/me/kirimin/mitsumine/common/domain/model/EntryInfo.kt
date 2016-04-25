package me.kirimin.mitsumine.common.domain.model

class EntryInfo {

    val title: String
    val bookmarkCount: Int
    val url: String
    val thumbnailUrl: String
    val bookmarkList: List<Bookmark>
    val tagList: List<String>

    constructor(title: String, bookmarkCount: Int, url: String, thumbnailUrl: String, bookmarkList: List<Bookmark>) {
        this.title = title
        this.bookmarkCount = bookmarkCount
        this.url = url
        this.thumbnailUrl = thumbnailUrl
        this.bookmarkList = bookmarkList
        this.tagList = bookmarkList
                .flatMap { it.tags }
                .groupBy { it }
                .map { it.value }
                .sortedByDescending { it.size }
                .take(4)
                .map { it[0] }
    }

    constructor() {
        this.title = "empty"
        this.bookmarkCount = 0
        this.url = ""
        this.thumbnailUrl = ""
        this.bookmarkList = emptyList()
        this.tagList = emptyList()
    }

    fun isNullObject() = title == "empty"

    fun tagListString() = tagList.joinToString(", ")
}
