package me.kirimin.mitsumine._common.network

import android.content.Context
import me.kirimin.mitsumine._common.domain.exceptions.ApiRequestException
import me.kirimin.mitsumine._common.domain.model.Star
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object StarApi {

    fun getCommentStar(context: Context, userId: String, timestamp: String, entryId: String): List<Star> {
        val date = timestamp.replace("/", "")
        val url = "http://s.hatena.com/entry.json?uri=http://b.hatena.ne.jp/$userId/$date%23bookmark-$entryId"
        try {
            val json = ApiAccessor.get(context, url)
            val stars = json.getJSONArray("entries").getJSONObject(0).getJSONArray("stars")
            val list = mutableListOf<Star>()
            for (i in 0..stars.length() - 1) {
                list.add(Star(stars.getJSONObject(i).getString("name")))
            }
            return list
        } catch (e: ApiRequestException) {
        }
        return emptyList<Star>()
    }

    fun requestCommentStar(context: Context, userId: String, timestamp: String, entryId: String): Observable<List<Star>> {
        val date = timestamp.replace("/", "")
        val url = "http://s.hatena.com/entry.json?uri=http://b.hatena.ne.jp/$userId/$date%23bookmark-$entryId"
        return ApiAccessor.request(context, url)
                .map {
                    val json = ApiAccessor.get(context, url)
                    val stars = json.getJSONArray("entries").getJSONObject(0).getJSONArray("stars")
                    val list = mutableListOf<Star>()
                    for (i in 0..stars.length() - 1) {
                        list.add(Star(stars.getJSONObject(i).getString("name")))
                    }
                    list.toList()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}