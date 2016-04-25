package me.kirimin.mitsumine.feed.read

import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.feed.read.ReadFeedRepository
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

class ReadFeedFragment : AbstractFeedFragment() {

    override fun getRepository(): AbstractFeedRepository = ReadFeedRepository(context, Feed.Companion.TYPE_READ)
    override fun isUseReadLater(): Boolean = true
    override fun isUseRead(): Boolean = false
}
