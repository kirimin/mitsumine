package me.kirimin.mitsumine.domain.model

import java.util.ArrayList

public class EntryInfo {

    public val title: String
    public val bookmarkCount: Int
    public val url: String
    public val thumbnailUrl: String
    public val bookmarkList: List<Bookmark>
    public val tagList: List<String>

    public constructor(title: String, bookmarkCount: Int, url: String, thumbnailUrl: String, bookmarkList: List<Bookmark>, tagList: List<String>) {
        this.title = title
        this.bookmarkCount = bookmarkCount
        this.url = url
        this.thumbnailUrl = thumbnailUrl
        this.bookmarkList = bookmarkList
        this.tagList = tagList
    }

    public constructor() {
        this.title = "empty"
        this.bookmarkCount = 0
        this.url = ""
        this.thumbnailUrl = ""
        this.bookmarkList = ArrayList()
        this.tagList = ArrayList()
    }

    public fun isNullObject(): Boolean {
        return title == "empty"
    }
}
