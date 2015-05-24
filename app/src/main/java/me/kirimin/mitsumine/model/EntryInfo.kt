package me.kirimin.mitsumine.model

import java.util.ArrayList

public class EntryInfo {

    public val title: String
    public val bookmarkCount: Int
    public val url: String
    public val thumbnailUrl: String
    public val bookmarkList: List<Bookmark>

    public constructor(title: String, bookmarkCount: Int, url: String, thumbnailUrl: String, bookmarkList: List<Bookmark>) {
        this.title = title
        this.bookmarkCount = bookmarkCount
        this.url = url
        this.thumbnailUrl = thumbnailUrl
        this.bookmarkList = bookmarkList
    }

    public constructor() {
        this.title = "empty"
        this.bookmarkCount = 0
        this.url = ""
        this.thumbnailUrl = ""
        this.bookmarkList = ArrayList()
    }

    public fun isNullObject(): Boolean {
        return title == "empty"
    }
}
