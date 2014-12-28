package me.kirimin.mitsumine.model;

import java.util.List;

public class EntryInfo {

    private final String title;
    private final int bookmarkCount;
    private final String url;
    private final String thumbnailUrl;
    private final List<Bookmark> bookmarkList;

    public EntryInfo(String title, int bookmarkCount, String url, String thumbnailUrl, List<Bookmark> bookmarkList) {
        this.title = title;
        this.bookmarkCount = bookmarkCount;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.bookmarkList = bookmarkList;
    }

    public String getTitle() {
        return title;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }
}
