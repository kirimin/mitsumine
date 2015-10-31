package me.kirimin.mitsumine.view.fragment

import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.domain.model.Feed

public class ReadFeedFragment : AbstractFeedFragment() {

    override fun requestFeed() {
        setFeed(FeedDAO.findByType(Feed.TYPE_READ))
        dismissRefreshing()
    }

    override fun isUseReadLater(): Boolean {
        return true
    }

    override fun isUseRead(): Boolean {
        return false
    }
}
