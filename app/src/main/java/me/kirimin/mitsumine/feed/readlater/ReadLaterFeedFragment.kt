package me.kirimin.mitsumine.feed.readlater

import android.os.Bundle
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.FeedPresenter

class ReadLaterFeedFragment : AbstractFeedFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.view = this
        presenter.onCreate(FeedPresenter.FeedMethod.ReadLatter())
    }
}
