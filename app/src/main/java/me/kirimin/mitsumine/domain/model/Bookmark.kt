package me.kirimin.mitsumine.domain.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

public class Bookmark : Parcelable {

    public val user: String
    public val tags: List<String>
    public val timeStamp: String
    public val comment: CharSequence
    public val userIcon: String
    private var isPrivate: Boolean = false

    public constructor(user: String, tags: List<String>, timeStamp: String, comment: CharSequence, userIcon: String) {
        this.user = user
        this.tags = tags
        this.timeStamp = timeStamp
        this.comment = comment
        this.userIcon = userIcon
    }

    public fun isPrivate(): Boolean {
        return isPrivate
    }

    public fun setPrivate(isPrivate: Boolean) {
        this.isPrivate = isPrivate
    }

    public fun hasComment(): Boolean {
        return !comment.toString().isEmpty()
    }

    protected constructor(`in`: Parcel) {
        user = `in`.readString()
        if (`in`.readByte() == 1.toByte()) {
            tags = ArrayList<String>()
            `in`.readList(tags, javaClass<String>().getClassLoader())
        } else {
            tags = ArrayList()
        }
        timeStamp = `in`.readString()
        comment = `in`.readValue(javaClass<CharSequence>().getClassLoader()) as CharSequence
        userIcon = `in`.readString()
        isPrivate = `in`.readByte() != 0.toByte()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(user)
        dest.writeByte((1).toByte())
        dest.writeList(tags)
        dest.writeString(timeStamp)
        dest.writeValue(comment)
        dest.writeString(userIcon)
        dest.writeByte((if (isPrivate) 1 else 0).toByte())
    }

    companion object {
        public val CREATOR: Parcelable.Creator<Bookmark> = object : Parcelable.Creator<Bookmark> {
            override fun createFromParcel(`in`: Parcel): Bookmark {
                return Bookmark(`in`)
            }

            override fun newArray(size: Int): Array<Bookmark?> {
                return arrayOfNulls(size)
            }
        }
    }
}