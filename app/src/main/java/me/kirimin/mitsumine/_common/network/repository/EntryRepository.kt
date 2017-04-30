package me.kirimin.mitsumine._common.network.repository

import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class EntryRepository @Inject constructor() {

    fun requestEntryInfo(url: String): Observable<EntryInfo>
            = Client.default(Client.EndPoint.API).build().create(HatenaBookmarkService::class.java).entryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map(::EntryInfo)
}