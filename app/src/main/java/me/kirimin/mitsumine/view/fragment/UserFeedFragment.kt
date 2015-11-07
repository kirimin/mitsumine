package me.kirimin.mitsumine.view.fragment

import android.os.Bundle
import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.UserFeedData

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