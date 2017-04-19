package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.network.EntryInfoApi
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import rx.Observable

class EntryInfoRepository {

    fun requestEntryInfoApi(context: Context, url: String): Observable<EntryInfo> = EntryInfoApi.request(context, url)

    fun isLogin(): Boolean = AccountDAO.get() != null
}