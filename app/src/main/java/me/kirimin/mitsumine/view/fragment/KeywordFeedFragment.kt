package me.kirimin.mitsumine.view.fragment

import android.os.Bundle

import me.kirimin.mitsumine.data.AbstractFeedData
import me.kirimin.mitsumine.data.KeyWordFeedData

public class KeywordFeedFragment : AbstractFeedFragment() {

    companion object {
        public fun newFragment(keyword: String): KeywordFeedFragment {
            val fragment = KeywordFeedFragment()
            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getDataInstance(): AbstractFeedData = KeyWordFeedData(context, arguments.getString("keyword"))

    override fun isUseReadLater(): Boolean = true

    override fun isUseRead(): Boolean = true
}