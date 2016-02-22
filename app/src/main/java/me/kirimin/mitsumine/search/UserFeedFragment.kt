package me.kirimin.mitsumine.search

import android.os.Bundle
import me.kirimin.mitsumine.feed.AbstractFeedData
import me.kirimin.mitsumine.search.UserFeedData
import me.kirimin.mitsumine.feed.AbstractFeedFragment

public class UserFeedFragment : AbstractFeedFragment() {

    companion object {
        public fun newFragment(user: String): UserFeedFragment {
            val fragment = UserFeedFragment()
            val bundle = Bundle()
            bundle.putString("user", user)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getDataInstance(): AbstractFeedData = UserFeedData(context, arguments.getString("user"))

    override fun isUseReadLater(): Boolean = true

    override fun isUseRead(): Boolean = true
}