package me.kirimin.mitsumine.mybookmark

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.domain.model.MyBookmark
import me.kirimin.mitsumine._common.network.repository.MyBookmarkRepository
import javax.inject.Inject

class MyBookmarkSearchUseCase @Inject constructor(val myBookmarkRepository: MyBookmarkRepository) {
    fun requestMyBookmarks(keyword: String, offset: Int) = myBookmarkRepository.requestMyBookmarks(account, keyword, offset)
            .map { Pair(it.bookmarks.map(::MyBookmark), it.meta.total) }!!

    private val account = AccountDAO.get()!!
}