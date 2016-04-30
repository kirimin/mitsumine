package me.kirimin.mitsumine.feed.read

import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.feed.read.ReadFeedRepository
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

class ReadFeedFragment : AbstractFeedFragment() {

    override fun getRepository() = ReadFeedRepository(context, Feed.Companion.TYPE_READ)
    override fun isUseReadLater() = true
    override fun isUseRead() = false
}
