package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine.common.database.AccountDAO
import me.kirimin.mitsumine.common.network.MyBookmarksApi
import me.kirimin.mitsumine.common.domain.model.MyBookmark
import rx.Observable

class MyBookmarkSearchRepository {
    fun requestMyBookmarks(keyword: String, offset: Int): Observable<MyBookmark> = MyBookmarksApi.request(AccountDAO.get()!!, keyword, offset)
}