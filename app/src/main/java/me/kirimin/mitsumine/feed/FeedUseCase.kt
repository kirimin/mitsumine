package me.kirimin.mitsumine.feed

import android.content.Context
import android.preference.PreferenceManager
import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.database.FeedDAO
import me.kirimin.mitsumine._common.database.NGWordDAO
import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.domain.enums.Category
import me.kirimin.mitsumine._common.domain.enums.Type
import me.kirimin.mitsumine._common.network.repository.BookmarkCountRepository
import me.kirimin.mitsumine._common.network.repository.EntryRepository
import me.kirimin.mitsumine._common.network.repository.FeedRepository
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class FeedUseCase @Inject
constructor(val context: Context,
            val feedRepository: FeedRepository,
            val entryRepository: EntryRepository,
            val bookmarkCountRepository: BookmarkCountRepository) {

    fun requestMainFeed(category: Category, type: Type) = feedRepository.requestFeed(category, type)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())!!

    fun requestKeywordFeed(keyword: String) = feedRepository.requestKeywordFeed(keyword)

    fun requestUserFeed(user: String) = feedRepository.requestUserFeed(user)

    fun requestReadFeed() = feedRepository.requestReadFeed()

    fun requestReadLatterFeed() = feedRepository.requestReadLatterFeed()

    val isUseBrowserSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_use_browser_to_comment_list), false)

    val isShareWithTitleSettingEnable: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_is_share_with_title), false)

    val ngWordList: List<String>
        get() = NGWordDAO.findAll()

    fun requestTagList(url: String) = entryRepository.requestEntryInfo(url).map { it.tagListString }!!

    fun requestBookmarkCount(url: String) = bookmarkCountRepository.requestBookmarkCount(url)

    fun saveFeed(feed: Feed) {
        FeedDAO.save(feed)
    }
}