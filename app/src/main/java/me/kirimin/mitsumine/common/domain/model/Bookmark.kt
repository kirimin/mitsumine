package me.kirimin.mitsumine.common.domain.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

class Bookmark : Parcelable {

    val user: String
    val tags: List<String>
    val timeStamp: String
    val comment: CharSequence
    val userIcon: String
    private var isPrivate: Boolean = false

    constructor(user: String, tags: List<String>, timeStamp: String, comment: CharSequence, userIcon: String) {
        this.user = user
        this.tags = tags
        this.timeStamp = timeStamp
        this.comment = comment
        this.userIcon = userIcon
    }

    fun isPrivate(): Boolean {
        return isPrivate
    }

    fun setPrivate(isPrivate: Boolean) {
        this.isPrivate = isPrivate
    }

    fun hasComment(): Boolean {
        return !comment.toString().isEmpty()
    }

    protected constructor(`in`: Parcel) {
        user = `in`.readString()
        if (`in`.readByte() == 1.toByte()) {
            tags = ArrayList<String>()
            `in`.readList(tags, String::class.java.classLoader)
        } else {
            tags = ArrayList()
        }
        timeStamp = `in`.readString()
        comment = `in`.readValue(CharSequence::class.java.classLoader) as CharSequence
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