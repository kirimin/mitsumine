package me.kirimin.mitsumine.feed

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.database.NGWordDAO
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.RetrofitClient
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

abstract class AbstractFeedRepository(val context: Context) {

     val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

     val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

     val ngWordList: List<String>
        get() = NGWordDAO.findAll()

     fun requestFeed() = getObservable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

    fun requestEntryInfo(url: String): Observable<EntryInfo>
            = RetrofitClient.default(RetrofitClient.EndPoint.API).build().create(HatenaBookmarkService::class.java).entryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map (::EntryInfo)

     fun requestBookmarkCount(url: String): Observable<String> =
             RetrofitClient.default(RetrofitClient.EndPoint.BOOKMARK_COUNT).build().create(HatenaBookmarkService::class.java).bookmarkCount(url)
             .subscribeOn(Schedulers.newThread())
             .observeOn(AndroidSchedulers.mainThread())

     fun saveFeed(feed: Feed) {
        FeedDAO.save(feed)
    }

    protected abstract fun getObservable(): Observable<Feed>
}