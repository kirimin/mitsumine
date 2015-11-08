package me.kirimin.mitsumine.data

import android.content.Context
import me.kirimin.mitsumine.data.network.api.EntryInfoApi
import me.kirimin.mitsumine.domain.model.EntryInfo
import rx.Observable

open class EntryInfoData {

    open fun requestEntryInfoApi(context: Context, url: String): Observable<EntryInfo> = EntryInfoApi.request(context, url)


}