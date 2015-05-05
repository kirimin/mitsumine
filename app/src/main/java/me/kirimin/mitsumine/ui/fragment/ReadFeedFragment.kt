package me.kirimin.mitsumine.ui.fragment

import me.kirimin.mitsumine.db.FeedDAO
import me.kirimin.mitsumine.model.Feed

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
