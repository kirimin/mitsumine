package me.kirimin.mitsumine.feed.mainfeed

import android.os.Bundle

import me.kirimin.mitsumine.feed.mainfeed.FeedRepository
import me.kirimin.mitsumine.common.domain.enums.Category
import me.kirimin.mitsumine.common.domain.enums.Type
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import java.io.Serializable

class FeedFragment : AbstractFeedFragment() {

    companion object {
        fun newFragment(category: Category, type: Type): FeedFragment {
            val fragment = FeedFragment()
            val bundle = Bundle()
            bundle.putSerializable(Category::class.java.canonicalName, category as Serializable)
            bundle.putSerializable(Type::class.java.canonicalName, type as Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getRepository(): FeedRepository {
        val category = arguments.getSerializable(Category::class.java.canonicalName) as Category
        val type = arguments.getSerializable(Type::class.java.canonicalName) as Type
        return FeedRepository(context, category, type)
    }

    override fun isUseReadLater(): Boolean = true

    override fun isUseRead(): Boolean = true
}