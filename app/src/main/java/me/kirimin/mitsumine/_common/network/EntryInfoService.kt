package me.kirimin.mitsumine._common.network

import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface EntryInfoService {

    @GET("/entry/jsonlite/")
    fun getEntryInfo(@Query(value = "url", encoded = true) url: String): Observable<EntryInfoResponse>
}