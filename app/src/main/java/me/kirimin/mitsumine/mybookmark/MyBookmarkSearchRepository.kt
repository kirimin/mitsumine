package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine._common.domain.model.MyBookmark
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.RetrofitClient
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MyBookmarkSearchRepository {
    fun requestMyBookmarks(keyword: String, offset: Int): Observable<Pair<List<MyBookmark>, Int>> =
            RetrofitClient.authClient(RetrofitClient.EndPoint.API, account).build()
                    .create(HatenaBookmarkService::class.java)
                    .myBookmarks(urlName = account.urlName, q = keyword, offSet = offset)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        when (it.code()) {
                            200 -> it.body()
                            401 -> throw ApiRequestException("401 auth error", it.code())
                            else -> throw ApiRequestException("error code=${it.code()}", it.code())
                        }
                    }
                    .map { Pair(it.bookmarks.map(::MyBookmark), it.meta.total) }

    private val account = AccountDAO.get()!!
}