package me.kirimin.mitsumine.model;

public class MyBookmark {

    private String title;
    private String comment;
    private int bookmarkCount;
    private String linkUrl;

    public MyBookmark(String title, String comment, int bookmarkCount, String linkUrl) {
        this.title = title;
        this.comment = comment;
        this.bookmarkCount = bookmarkCount;
        this.linkUrl = linkUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
}
