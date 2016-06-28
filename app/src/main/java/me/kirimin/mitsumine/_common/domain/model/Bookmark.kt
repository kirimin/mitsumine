package me.kirimin.mitsumine._common.domain.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

class Bookmark : Parcelable {

    val user: String
    val tags: List<String>
    val timeStamp: String
    val comment: CharSequence
    val userIcon: String
    val stars: List<Star>
    private var isPrivate: Boolean = false

    constructor(user: String, tags: List<String>, timeStamp: String, comment: CharSequence, userIcon: String, stars: List<Star>) {
        this.user = user
        this.tags = tags
        this.timeStamp = timeStamp
        this.comment = comment
        this.userIcon = userIcon
        this.stars = stars
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
        if (`in`.readByte() == 1.toByte()) {
            stars = ArrayList<Star>()
            `in`.readList(stars, String::class.java.classLoader)
        } else {
            stars = ArrayList()
        }
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
        dest.writeByte((1).toByte())
        dest.writeList(stars)
        dest.writeByte((if (isPrivate) 1 else 0).toByte())
    }

    companion object {

        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<Bookmark> = object : Parcelable.Creator<Bookmark> {
            override fun createFromParcel(`in`: Parcel): Bookmark {
                return Bookmark(`in`)
            }

            override fun newArray(size: Int): Array<Bookmark?> {
                return arrayOfNulls(size)
            }
        }
    }
}