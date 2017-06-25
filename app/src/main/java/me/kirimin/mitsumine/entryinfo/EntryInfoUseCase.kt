package me.kirimin.mitsumine.entryinfo

import me.kirimin.mitsumine._common.database.AccountDAO
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.domain.model.EntryInfo
import me.kirimin.mitsumine._common.domain.model.Stars
import me.kirimin.mitsumine._common.network.HatenaBookmarkService
import me.kirimin.mitsumine._common.network.Client
import me.kirimin.mitsumine._common.network.entity.StarOfBookmarkResponse
import me.kirimin.mitsumine._common.network.repository.StarRepository
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class EntryInfoUseCase @Inject constructor(private val starRepository: StarRepository) {

    fun requestEntryInfo(url: String): Observable<EntryInfo>
            = Client.default(Client.EndPoint.API).build().create(HatenaBookmarkService::class.java).entryInfo(url)
            .subscribeOn(Schedulers.newThread())
            .map(::EntryInfo)
            .observeOn(AndroidSchedulers.mainThread())

    fun isLogin(): Boolean = AccountDAO.get() != null

    fun loadStars(entryInfo: EntryInfo): Observable<EntryInfo> {
        return Observable.from(entryInfo.bookmarkList)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .filter { it.hasComment }
                .flatMap { starRepository.requestCommentStar(it.user, it.timestamp, entryInfo.entryId).onErrorReturn { Stars() } }
                .toList()
                .map { stars ->
                    entryInfo.bookmarkList.filter { it.hasComment }.forEachIndexed { index, bookmark -> bookmark.stars = stars[index] }
                    entryInfo
                }
    }
}