package me.kirimin.mitsumine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Bookmark implements Parcelable {

    private final String user;
    private final List<String> tags;
    private final String timeStamp;
    private final CharSequence comment;
    private final String userIcon;
    private boolean isPrivate;

    public Bookmark(String user, List<String> tags, String timeStamp, CharSequence comment, String userIcon) {
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

    public CharSequence getComment() {
        return comment;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    protected Bookmark(Parcel in) {
        user = in.readString();
        if (in.readByte() == 0x01) {
            tags = new ArrayList<>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
        timeStamp = in.readString();
        comment = (CharSequence) in.readValue(CharSequence.class.getClassLoader());
        userIcon = in.readString();
        isPrivate = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeString(timeStamp);
        dest.writeValue(comment);
        dest.writeString(userIcon);
        dest.writeByte((byte) (isPrivate ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bookmark> CREATOR = new Parcelable.Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel in) {
            return new Bookmark(in);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };
}