package me.kirimin.mitsumine.feed

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.database.FeedDAO
import me.kirimin.mitsumine.common.database.NGWordDAO
import me.kirimin.mitsumine.common.network.BookmarkCountApi
import me.kirimin.mitsumine.common.network.TagListApi
import me.kirimin.mitsumine.common.domain.model.Feed
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

abstract class AbstractFeedRepository(val context: Context) {

    open val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

    open val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

    open val readFeedList: List<Feed>
        get() = FeedDAO.findAll()

    open val ngWordList: List<String>
        get() = NGWordDAO.findAll()

    open fun requestFeed() = getObservable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

    open fun requestTagList(url: String): Observable<List<String>> = TagListApi.request(context.applicationContext, url)

    open fun requestBookmarkCount(url: String): Observable<String> = BookmarkCountApi.request(context.applicationContext, url)

    open fun saveFeed(feed: Feed) {
        FeedDAO.save(feed)
    }

    protected abstract fun getObservable(): Observable<Feed>
}