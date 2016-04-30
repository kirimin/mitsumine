package me.kirimin.mitsumine._common.domain.util

import me.kirimin.mitsumine._common.domain.model.Feed

object FeedUtil {

    fun contains(target: Feed, list: List<Feed>): Boolean {
        return list.filter { f -> f.title == target.title }.count() >= 1
    }

    fun containsWord(target: Feed, list: List<String>): Boolean {
        return list.filter { s -> target.title.contains(s) || target.linkUrl.contains(s) }.count() >= 1
    }
}
