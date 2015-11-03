package me.kirimin.mitsumine.data

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.network.api.FeedApi
import me.kirimin.mitsumine.model.Feed
import me.kirimin.mitsumine.model.enums.Category
import me.kirimin.mitsumine.model.enums.Type
import rx.Observable

open class FeedData(val context: Context) {
    open fun requestFeed(category: Category, type: Type): Observable<Feed> = FeedApi.requestCategory(context, category, type)

    open val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

    open val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

    fun saveFeed(feed: Feed) {
        feed.save()
    }
}