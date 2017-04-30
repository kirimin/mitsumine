package me.kirimin.mitsumine.feed.read

import android.os.Bundle
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.FeedPresenter

class ReadFeedFragment : AbstractFeedFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.view = this
        presenter.onCreate(FeedPresenter.FeedMethod.Read())
    }
}
