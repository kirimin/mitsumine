package me.kirimin.mitsumine.feed

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.database.NGWordDAO
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.network.BookmarkCountApi
import me.kirimin.mitsumine._common.network.EntryInfoService
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.network.RetrofitClient
import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse
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
            = RetrofitClient.default(RetrofitClient.EndPoint.ENTRY_INFO).build().create(EntryInfoService::class.java).getEntryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map (::EntryInfo)

     fun requestBookmarkCount(url: String): Observable<String> = BookmarkCountApi.request(context.applicationContext, url)

     fun saveFeed(feed: Feed) {
        FeedDAO.save(feed)
    }

    protected abstract fun getObservable(): Observable<Feed>
}