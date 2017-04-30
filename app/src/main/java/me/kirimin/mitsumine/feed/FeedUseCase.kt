package me.kirimin.mitsumine.feed

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.database.NGWordDAO
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.domain.model.Feed.Companion.TYPE_READ
import me.kirimin.mitsumine._common.domain.model.Feed.Companion.TYPE_READ_LATER
import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.repository.FeedRepository
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class FeedUseCase @Inject constructor(val context: Context, val feedRepository: FeedRepository) {

    fun requestMainFeed(category: Category, type: Type) = feedRepository.requestFeed(category, type)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestKeywordFeed(keyword: String) = feedRepository.requestKeywordFeed(keyword)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestUserFeed(user: String) = feedRepository.requestUserFeed(user)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestReadFeed() = Observable.from(FeedDAO.findByType(TYPE_READ))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestReadLatterFeed() = Observable.from(FeedDAO.findByType(TYPE_READ_LATER))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

    val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

    val ngWordList: List<String>
        get() = NGWordDAO.findAll()

    fun requestTagList(url: String): Observable<String>
            = Client.default(Client.EndPoint.API).build().create(HatenaBookmarkService::class.java).entryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map(::EntryInfo)
            .map { it.tagListString }

    fun requestBookmarkCount(url: String): Observable<String> =
            Client.default(Client.EndPoint.BOOKMARK_COUNT).build().create(HatenaBookmarkService::class.java).bookmarkCount(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

    fun saveFeed(feed: Feed) {
        FeedDAO.save(feed)
    }
}