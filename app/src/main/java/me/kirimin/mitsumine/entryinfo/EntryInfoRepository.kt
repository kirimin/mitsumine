package me.kirimin.mitsumine.entryinfo

import android.content.Context
import me.kirimin.mitsumine.common.network.EntryInfoApi
import me.kirimin.mitsumine.common.domain.model.EntryInfo
import rx.Observable

open class EntryInfoRepository {

    open fun requestEntryInfoApi(context: Context, url: String): Observable<EntryInfo> = EntryInfoApi.request(context, url)


}