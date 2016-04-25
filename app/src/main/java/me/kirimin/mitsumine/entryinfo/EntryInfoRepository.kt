package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine.common.database.AccountDAO
import me.kirimin.mitsumine.common.network.EntryInfoApi
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class EntryInfoRepository {

    open fun requestEntryInfoApi(context: Context, url: String): Observable<EntryInfo> = EntryInfoApi.request(context, url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

    open fun isLogin(): Boolean = AccountDAO.get() != null
}