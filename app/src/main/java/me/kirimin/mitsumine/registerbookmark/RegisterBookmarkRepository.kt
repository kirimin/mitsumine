package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.network.BookmarkApi
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class RegisterBookmarkRepository {

    open fun requestBookmarkInfo(url: String) = BookmarkApi.requestBookmarkInfo(url, AccountDAO.get()!!)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

    open fun requestRegisterBookmark(url: String, comment: String, tags: List<String>, isPrivate: Boolean, isTwitter: Boolean) =
            BookmarkApi.requestAddBookmark(url, AccountDAO.get()!!, comment, tags, isPrivate, isTwitter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())

    open fun requestDeleteBookmark(url: String) = BookmarkApi.requestDeleteBookmark(url, AccountDAO.get()!!)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
}