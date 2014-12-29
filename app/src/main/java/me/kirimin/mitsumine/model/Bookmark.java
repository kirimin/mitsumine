package me.kirimin.mitsumine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Bookmark implements Parcelable {

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

    protected Bookmark(Parcel in) {
        user = in.readString();
        if (in.readByte() == 0x01) {
            tags = new ArrayList<String>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
        timeStamp = in.readString();
        comment = in.readString();
        userIcon = in.readString();
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
        dest.writeString(comment);
        dest.writeString(userIcon);
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
