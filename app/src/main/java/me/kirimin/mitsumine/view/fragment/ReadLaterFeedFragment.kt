package me.kirimin.mitsumine.view.fragment

import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.domain.model.Feed

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
