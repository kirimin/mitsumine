package me.kirimin.mitsumine.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

Table(name = "feed")
public class Feed : Model() {

    Column(name = "title")
    public var title: String = ""

    Column(name = "thumbnailUrl")
    public var thumbnailUrl: String = ""

    Column(name = "content")
    public var content: String = ""

    Column(name = "linkUrl", unique = true)
    public var linkUrl: String = ""

    Column(name = "entryLinkUrl")
    public var entryLinkUrl: String = ""

    Column(name = "bookmarkCountUrl")
    public var bookmarkCountUrl: String = ""

    Column(name = "faviconUrl")
    public var faviconUrl: String = ""

    Column(name = "type")
    public var type: String = ""

    Column(name = "saveTime")
    public var saveTime: Long = 0

    override fun toString(): String {
        return StringBuilder().append("title:").append(title).append(" type:").append(type).toString()
    }

    companion object {

        public val TYPE_READ: String = "read"
        public val TYPE_READ_LATER: String = "readlater"
    }
}