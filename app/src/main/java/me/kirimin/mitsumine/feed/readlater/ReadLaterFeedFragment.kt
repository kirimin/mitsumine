package me.kirimin.mitsumine.feed.readlater

import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.feed.read.ReadFeedRepository
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

class ReadLaterFeedFragment : AbstractFeedFragment() {

    override fun getRepository(): AbstractFeedRepository = ReadFeedRepository(context, Feed.Companion.TYPE_READ_LATER)
    override fun isUseReadLater(): Boolean = false
    override fun isUseRead(): Boolean = true
}
