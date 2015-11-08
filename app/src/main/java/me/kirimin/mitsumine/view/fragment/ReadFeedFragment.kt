package me.kirimin.mitsumine.view.fragment

import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.ReadFeedData
import me.kirimin.mitsumine.domain.model.Feed

public class ReadFeedFragment : AbstractFeedFragment() {

    override fun getDataInstance(): AbstractFeedData = ReadFeedData(context, Feed.TYPE_READ)
    override fun isUseReadLater(): Boolean = true
    override fun isUseRead(): Boolean = false
}
