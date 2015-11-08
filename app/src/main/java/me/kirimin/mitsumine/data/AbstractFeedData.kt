package me.kirimin.mitsumine.data

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.FeedDAO
import me.kirimin.mitsumine.data.database.NGWordDAO
import me.kirimin.mitsumine.domain.model.Feed
import rx.Observable

abstract class AbstractFeedData(val context: Context) {

    val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

    val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

    abstract fun requestFeed(): Observable<Feed>

    fun saveFeed(feed: Feed) {
        feed.save()
    }
}