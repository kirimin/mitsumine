package me.kirimin.mitsumine.ui.fragment

import me.kirimin.mitsumine.db.FeedDAO
import me.kirimin.mitsumine.model.Feed

public class ReadLaterFeedFragment : AbstractFeedFragment() {

    override fun requestFeed() {
        setFeed(FeedDAO.findByType(Feed.TYPE_READ_LATER))
        dismissRefreshing()
    }

    override fun isUseReadLater(): Boolean {
        return false
    }

    override fun isUseRead(): Boolean {
        return true
    }
}
