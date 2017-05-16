package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.network.repository.BookmarkRepository
import javax.inject.Inject

class RegisterBookmarkUseCase @Inject constructor(val bookmarkRepository: BookmarkRepository) {

    fun requestBookmarkInfo(url: String) = bookmarkRepository.requestBookmarkInfo(url)

    fun requestAddBookmark(url: String, comment: String, tags: List<String>, isPrivate: Boolean, isTwitter: Boolean) =
            bookmarkRepository.requestAddBookmark(url, comment, tags, isPrivate, isTwitter)

    fun requestDeleteBookmark(url: String) = bookmarkRepository.requestDeleteBookmark(url)
}