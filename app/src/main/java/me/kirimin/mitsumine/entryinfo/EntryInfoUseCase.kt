package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.Client
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class EntryInfoUseCase {

    fun requestEntryInfo(url: String): Observable<EntryInfo>
            = Client.default(Client.EndPoint.API).build().create(HatenaBookmarkService::class.java).entryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map (::EntryInfo)

    fun isLogin(): Boolean = AccountDAO.get() != null
}