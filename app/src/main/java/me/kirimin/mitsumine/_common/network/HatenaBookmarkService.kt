package me.kirimin.mitsumine._common.network

import me.kirimin.mitsumine._common.network.entity.BookmarkResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface HatenaBookmarkService {

    @GET("/1/my/bookmark")
    fun requestBookmark(@Query(value = "url", encoded = true) url: String): Observable<Response<BookmarkResponse>>

    @POST("/1/my/bookmark")
    fun addBookmark(@Query(value = "url", encoded = true) url: String,
                    @Query("comment") comment: String? = null,
                    @Query("tags") tags: List<String> = emptyList(),
                    @Query("private") private: Boolean = false,
                    @Query("post_twitter") postTwitter: Boolean = false): Observable<Response<BookmarkResponse>>

    @DELETE("/1/my/bookmark")
    fun deleteBookmark(@Query(value = "url", encoded = true) url: String): Observable<Response<Boolean>>
}