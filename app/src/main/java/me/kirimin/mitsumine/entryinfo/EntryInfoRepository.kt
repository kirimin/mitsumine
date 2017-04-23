package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.network.EntryInfoService
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.network.RetrofitClient
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class EntryInfoRepository {

    fun requestEntryInfo(url: String): Observable<EntryInfo>
            = RetrofitClient.default(RetrofitClient.EndPoint.ENTRY_INFO).build().create(EntryInfoService::class.java).getEntryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map (::EntryInfo)

    fun isLogin(): Boolean = AccountDAO.get() != null
}