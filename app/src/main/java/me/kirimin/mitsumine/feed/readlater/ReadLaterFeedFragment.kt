package me.kirimin.mitsumine.feed.readlater

import me.kirimin.mitsumine.feed.AbstractFeedData
import me.kirimin.mitsumine.feed.read.ReadFeedData
import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

public class ReadLaterFeedFragment : AbstractFeedFragment() {

    override fun getDataInstance(): AbstractFeedData = ReadFeedData(context, Feed.Companion.TYPE_READ_LATER)
    override fun isUseReadLater(): Boolean = false
    override fun isUseRead(): Boolean = true
}
