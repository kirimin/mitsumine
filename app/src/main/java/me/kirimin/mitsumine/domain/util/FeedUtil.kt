package me.kirimin.mitsumine.domain.util

import me.kirimin.mitsumine.domain.model.Feed

public class FeedUtil {
    companion object {

        public fun contains(target: Feed, list: List<Feed>): Boolean {
            return list.filter { f -> f.title == target.title }.count() >= 1
        }

        public fun containsWord(target: Feed, list: List<String>): Boolean {
            return list.filter { s -> target.title.contains(s) || target.linkUrl.contains(s) }.count() >= 1
        }
    }

}
