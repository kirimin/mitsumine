package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.URLEncoder
import javax.inject.Inject

class BookmarkRepository @Inject constructor() {

    fun requestBookmarkInfo(url: String) =
            Client.authClient(Client.EndPoint.REST_API, AccountDAO.get()!!).build()
                    .create(HatenaBookmarkService::class.java).bookmark(URLEncoder.encode(url, "utf-8"))
                    .subscribeOn(Schedulers.newThread())
                    .map { responseHandling(it) }!!
                    .observeOn(AndroidSchedulers.mainThread())!!

    fun requestAddBookmark(url: String, comment: String, tags: List<String>, isPrivate: Boolean, isTwitter: Boolean) =
            Client.authClient(Client.EndPoint.REST_API, AccountDAO.get()!!).build()
                    .create(HatenaBookmarkService::class.java).addBookmark(url = URLEncoder.encode(url, "utf-8"), comment = comment, tags = tags, private = if(isPrivate) 1 else 0, postTwitter = if(isTwitter) 1 else 0)
                    .subscribeOn(Schedulers.newThread())
                    .map { responseHandling(it) }!!
                    .observeOn(AndroidSchedulers.mainThread())!!

    fun requestDeleteBookmark(url: String) =
            Client.authClient(Client.EndPoint.REST_API, AccountDAO.get()!!).build()
                    .create(HatenaBookmarkService::class.java).deleteBookmark(URLEncoder.encode(url, "utf-8"))
                    .subscribeOn(Schedulers.newThread())
                    .map {
                        when (it.code()) {
                            204 -> it.body()
                            401 -> throw ApiRequestException("401 auth error", it.code())
                            else -> throw ApiRequestException("error code=${it.code()}", it.code())
                        }
                    }!!
                    .observeOn(AndroidSchedulers.mainThread())!!

    private fun responseHandling(it: Response<BookmarkResponse>): Bookmark {
        return when (it.code()) {
            200 -> Bookmark(it.body())
            404 -> Bookmark.EmptyBookmark()
            401 -> throw ApiRequestException("401 auth error", it.code())
            else -> throw ApiRequestException("error code=${it.code()}", it.code())
        }
    }
}
