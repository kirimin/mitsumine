package me.kirimin.mitsumine.feed.mainfeed

import android.os.Bundle

import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine.feed.AbstractFeedFragment
import me.kirimin.mitsumine.feed.FeedPresenter
import java.io.Serializable

class MainFeedFragment : AbstractFeedFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val category = arguments.getSerializable(Category::class.java.canonicalName) as Category
        val type = arguments.getSerializable(Type::class.java.canonicalName) as Type
        presenter.view = this
        presenter.onCreate(FeedPresenter.FeedMethod.MainFeed(category = category, type = type))
    }

    companion object {
        fun newFragment(category: Category, type: Type): MainFeedFragment {
            val fragment = MainFeedFragment()
            val bundle = Bundle()
            bundle.putSerializable(Category::class.java.canonicalName, category as Serializable)
            bundle.putSerializable(Type::class.java.canonicalName, type as Serializable)
            fragment.arguments = bundle
            return fragment
        }
    }
}