package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine._common.domain.model.Account
import me.kirimin.mitsumine._common.domain.model.MyBookmark
import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.entity.MyBookmarksResponse
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class MyBookmarkRepository @Inject constructor() {

    fun requestMyBookmarks(account: Account, keyword: String, offset: Int): Observable<MyBookmarksResponse> =
            Client.authClient(Client.EndPoint.API, account).build().create(HatenaBookmarkService::class.java)
                    .myBookmarks(urlName = account.urlName, q = keyword, offSet = offset)
                    .subscribeOn(Schedulers.newThread())
                    .map {
                        when (it.code()) {
                            200 -> it.body()
                            401 -> throw ApiRequestException("401 auth error", it.code())
                            else -> throw ApiRequestException("error code=${it.code()}", it.code())
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
}