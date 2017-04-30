package me.kirimin.mitsumine.feed.keyword

import android.os.Bundle

import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.FeedPresenter

class KeywordSearchFragment : AbstractFeedFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.view = this
        presenter.onCreate(FeedPresenter.FeedMethod.KeywordSearch(arguments.getString("keyword", "")))
    }

    companion object {
        fun newFragment(keyword: String): KeywordSearchFragment {
            val fragment = KeywordSearchFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.arguments = bundle
            return fragment
        }
    }
}