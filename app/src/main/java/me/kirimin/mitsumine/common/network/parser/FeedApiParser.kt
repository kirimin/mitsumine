package me.kirimin.mitsumine.common.network.parser

import me.kirimin.mitsumine.common.domain.model.Feed
import me.kirimin.mitsumine.common.network.entity.RssRoot
import org.simpleframework.xml.core.Persister
import rx.Observable

object FeedApiParser {

    fun parseResponse(response: String): Observable<Feed> {
        try {
            val xmlRoot = Persister().read(RssRoot::class.java, response, false)
            return Observable.from(xmlRoot.itemList.map { Feed(it) })
        } catch(e: Exception) {
            return Observable.empty()
        }
    }
}