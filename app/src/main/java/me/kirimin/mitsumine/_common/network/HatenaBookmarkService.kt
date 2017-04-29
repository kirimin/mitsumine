package me.kirimin.mitsumine._common.network

import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import me.kirimin.mitsumine._common.network.entity.EntryInfoResponse
import me.kirimin.mitsumine._common.network.entity.MyBookmarksResponse
import me.kirimin.mitsumine._common.network.entity.StarOfBookmarkResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface HatenaBookmarkService {

    @GET("/1/my/bookmark")
    fun bookmark(@Query(value = "url", encoded = true) url: String): Observable<Response<BookmarkResponse>>

    @POST("/1/my/bookmark")
    fun addBookmark(@Query(value = "url", encoded = true) url: String,
                    @Query("comment") comment: String? = null,
                    @Query("tags") tags: List<String> = emptyList(),
                    @Query("private") private: Boolean = false,
                    @Query("post_twitter") postTwitter: Boolean = false): Observable<Response<BookmarkResponse>>

    @DELETE("/1/my/bookmark")
    fun deleteBookmark(@Query(value = "url", encoded = true) url: String): Observable<Response<Boolean>>

    @GET("/entry/jsonlite/")
    fun entryInfo(@Query(value = "url", encoded = true) url: String): Observable<EntryInfoResponse>

    @GET("/entry.json")
    fun starOfBookmark(@Query(value = "uri", encoded = true) uri: String): Observable<StarOfBookmarkResponse>

    @GET("/entry.count")
    fun bookmarkCount(@Query(value = "url", encoded = true) url: String): Observable<String>

    @GET("{urlName}/search/json")
    fun myBookmarks(@Path("urlName") urlName: String,
                    @Query("of") offSet: Int,
                    @Query("q") q: String = "",
                    @Query("limit") limit: Int = 50,
                    @Query("sort") sort: String = "") : Observable<Response<MyBookmarksResponse>>
}