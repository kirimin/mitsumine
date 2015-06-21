package me.kirimin.mitsumine.util

import me.kirimin.mitsumine.model.Bookmark

class EntryInfoUtil {
    companion object {

        public fun hasComment(bookmark: Bookmark): Boolean {
            return !bookmark.comment.toString().isEmpty()
        }

    }
}