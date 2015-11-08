package me.kirimin.mitsumine.view.fragment

import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.ReadFeedData
import me.kirimin.mitsumine.domain.model.Feed

public class ReadLaterFeedFragment : AbstractFeedFragment() {

    override fun getDataInstance(): AbstractFeedData = ReadFeedData(context, Feed.TYPE_READ_LATER)
    override fun isUseReadLater(): Boolean = false
    override fun isUseRead(): Boolean = true
}
