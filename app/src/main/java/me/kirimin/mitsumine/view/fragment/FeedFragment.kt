package me.kirimin.mitsumine.view.fragment

import android.os.Bundle

import me.kirimin.mitsumine.data.FeedData
import me.kirimin.mitsumine.model.enums.Category
import me.kirimin.mitsumine.model.enums.Type
import java.io.Serializable

public class FeedFragment : AbstractFeedFragment() {

    companion object {
        public fun newFragment(category: Category, type: Type): FeedFragment {
            val fragment = FeedFragment()
            val bundle = Bundle()
            bundle.putSerializable(Category::class.java.canonicalName, category as Serializable)
            bundle.putSerializable(Type::class.java.canonicalName, type as Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getDataInstance(): FeedData {
        val category = arguments.getSerializable(Category::class.java.canonicalName) as Category
        val type = arguments.getSerializable(Type::class.java.canonicalName) as Type
        return FeedData(context, category, type)
    }

    override fun isUseReadLater(): Boolean = true

    override fun isUseRead(): Boolean = true
}