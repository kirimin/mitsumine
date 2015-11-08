package me.kirimin.mitsumine.data

import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.network.api.MyBookmarksApi
import me.kirimin.mitsumine.domain.model.MyBookmark
import rx.Observable

class MyBookmarksData {
    fun requestMyBookmarks(keyword: String, offset: Int): Observable<MyBookmark> = MyBookmarksApi.request(AccountDAO.get()!!, keyword, offset)
}