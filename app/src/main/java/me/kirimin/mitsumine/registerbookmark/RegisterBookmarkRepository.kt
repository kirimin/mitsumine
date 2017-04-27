package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.RetrofitClient
import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.URLEncoder

class RegisterBookmarkRepository {

    fun requestBookmarkInfo(url: String) =
            RetrofitClient.authClient(RetrofitClient.EndPoint.HATENA_BOOKMARK_API, AccountDAO.get()!!).build()
                    .create(HatenaBookmarkService::class.java).requestBookmark(URLEncoder.encode(url, "utf-8"))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { responseHandling(it) }!!

    fun requestAddBookmark(url: String, comment: String, tags: List<String>, isPrivate: Boolean, isTwitter: Boolean) =
            RetrofitClient.authClient(RetrofitClient.EndPoint.HATENA_BOOKMARK_API, AccountDAO.get()!!).build()
                    .create(HatenaBookmarkService::class.java).addBookmark(url = URLEncoder.encode(url, "utf-8"), comment = comment, tags = tags, private = isPrivate, postTwitter = isTwitter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { responseHandling(it) }!!

    fun requestDeleteBookmark(url: String) =
            RetrofitClient.authClient(RetrofitClient.EndPoint.HATENA_BOOKMARK_API, AccountDAO.get()!!).build()
                    .create(HatenaBookmarkService::class.java).deleteBookmark(URLEncoder.encode(url, "utf-8"))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())!!
                    .map {
                        when (it.code()) {
                            204 -> it.body()
                            401 -> throw ApiRequestException("401 auth error", it.code())
                            else -> throw ApiRequestException("error code=${it.code()}", it.code())
                        }
                    }!!

    private fun responseHandling(it: Response<BookmarkResponse>): Bookmark {
        return when (it.code()) {
            200 -> Bookmark(it.body())
            404 -> Bookmark.EmptyBookmark()
            401 -> throw ApiRequestException("401 auth error", it.code())
            else -> throw ApiRequestException("error code=${it.code()}", it.code())
        }
    }
}