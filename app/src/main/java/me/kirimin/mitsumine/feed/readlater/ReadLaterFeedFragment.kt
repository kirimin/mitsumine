package me.kirimin.mitsumine.feed.readlater

import me.kirimin.mitsumine.feed.read.ReadFeedUseCase
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine.feed.AbstractFeedFragment

class ReadLaterFeedFragment : AbstractFeedFragment() {

    override fun getRepository() = ReadFeedUseCase(context, Feed.Companion.TYPE_READ_LATER)
    override fun isUseReadLater() = false
    override fun isUseRead() = true
}
