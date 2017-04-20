package me.kirimin.mitsumine._common.network.parser

import me.kirimin.mitsumine._common.domain.model.Feed
import me.kirimin.mitsumine._common.network.entity.FeedRssRoot
import org.simpleframework.xml.core.Persister
import rx.Observable

object FeedApiParser {

    fun parseResponse(response: String): Observable<Feed> {
        try {
            val xmlRoot = Persister().read(FeedRssRoot::class.java, response, false)
            return Observable.from(xmlRoot.itemList.map(::Feed))
        } catch(e: Exception) {
            return Observable.error(e)
        }
    }
}