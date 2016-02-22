package me.kirimin.mitsumine.feed.read

import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.feed.read.ReadFeedRepository
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

public class ReadFeedFragment : AbstractFeedFragment() {

    override fun getDataInstance(): AbstractFeedRepository = ReadFeedRepository(context, Feed.Companion.TYPE_READ)
    override fun isUseReadLater(): Boolean = true
    override fun isUseRead(): Boolean = false
}
