package me.kirimin.mitsumine.feed.read

import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

class ReadFeedFragment : AbstractFeedFragment() {

    override fun getRepository() = ReadFeedUseCase(context, Feed.Companion.TYPE_READ)
    override fun isUseReadLater() = true
    override fun isUseRead() = false
}
