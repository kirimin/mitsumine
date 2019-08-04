package me.kirimin.mitsumine.feed.user

import android.os.Bundle
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.FeedPresenter

class UserSearchFragment : AbstractFeedFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.view = this
        presenter.onCreate(FeedPresenter.FeedMethod.UserSearch(arguments!!.getString("user", "")))
    }

    companion object {
        fun newFragment(user: String): UserSearchFragment {
            val fragment = UserSearchFragment()
            val bundle = Bundle()
            bundle.putString("user", user)
            fragment.arguments = bundle
            return fragment
        }
    }
}