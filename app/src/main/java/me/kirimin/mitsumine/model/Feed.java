package me.kirimin.mitsumine.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "feed")
public class Feed extends Model {

    public static String TYPE_READ = "read";
    public static String TYPE_READ_LATER = "readlater";

    @Column(name = "title")
    public String title = "";

    @Column(name = "thumbnailUrl")
    public String thumbnailUrl = "";

    @Column(name = "content")
    public String content = "";

    @Column(name = "linkUrl", unique = true)
    public String linkUrl = "";

    @Column(name = "entryLinkUrl")
    public String entryLinkUrl = "";

    @Column(name = "bookmarkCountUrl")
    public String bookmarkCountUrl = "";

    @Column(name = "faviconUrl")
    public String faviconUrl = "";

    @Column(name = "type")
    public String type;

    @Column(name = "saveTime")
    public long saveTime;

    @Override
    public String toString() {
        return new StringBuilder().append("title:").append(title).append(" type:").append(type).toString();
    }
}