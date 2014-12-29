package me.kirimin.mitsumine.model;

import java.util.List;

public class Bookmark {

    private final String user;
    private final List<String> tags;
    private final String timeStamp;
    private final String comment;
    private final String userIcon;

    public Bookmark(String user, List<String> tags, String timeStamp, String comment, String userIcon) {
        this.user = user;
        this.tags = tags;
        this.timeStamp = timeStamp;
        this.comment = comment;
        this.userIcon = userIcon;
    }

    public String getUser() {
        return user;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public String getUserIcon() {
        return userIcon;
    }
}
