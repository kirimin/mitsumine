package me.kirimin.mitsumine.search

import android.os.Bundle

import me.kirimin.mitsumine.feed.AbstractFeedRepository
import me.kirimin.mitsumine.search.KeyWordFeedRepository
import me.kirimin.mitsumine.feed.AbstractFeedFragment

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

    override fun getRepository(): AbstractFeedRepository = KeyWordFeedRepository(context, arguments.getString("keyword"))

    override fun isUseReadLater(): Boolean = true

    override fun isUseRead(): Boolean = true
}