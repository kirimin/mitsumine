package me.kirimin.mitsumine.feed.read

import me.kirimin.mitsumine.feed.AbstractFeedData
import me.kirimin.mitsumine.feed.read.ReadFeedData
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

public class ReadFeedFragment : AbstractFeedFragment() {

    override fun getDataInstance(): AbstractFeedData = ReadFeedData(context, Feed.Companion.TYPE_READ)
    override fun isUseReadLater(): Boolean = true
    override fun isUseRead(): Boolean = false
}
