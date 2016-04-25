package me.kirimin.mitsumine.feed.user

import android.os.Bundle
import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.feed.user.UserSearchRepository
import me.kirimin.mitsumine.feed.AbstractFeedFragment

public class UserSearchFragment : AbstractFeedFragment() {

    companion object {
        public fun newFragment(user: String): UserSearchFragment {
            val fragment = UserSearchFragment()
            val bundle = Bundle()
            bundle.putString("user", user)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getRepository(): AbstractFeedRepository = UserSearchRepository(context, arguments.getString("user"))

    override fun isUseReadLater(): Boolean = true

    override fun isUseRead(): Boolean = true
}