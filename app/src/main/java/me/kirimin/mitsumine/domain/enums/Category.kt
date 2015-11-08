package me.kirimin.mitsumine.domain.enums

import me.kirimin.mitsumine.R

public enum class Category private constructor(private val url: String, public val labelResource: Int) {
    MAIN("", R.string.feed_main),
    SOCIAL("/social", R.string.feed_social),
    ECONOMICS("/economics", R.string.feed_economics),
    LIFE("/life", R.string.feed_life),
    KNOWLEDGE("/knowledge", R.string.feed_knowledge),
    IT("/it", R.string.feed_it),
    FUN("/fun", R.string.feed_fun),
    ENTERTAINMENT("/entertainment", R.string.feed_entertainment),
    GAME("/game", R.string.feed_game);

    override fun toString(): String {
        return url
    }
}
