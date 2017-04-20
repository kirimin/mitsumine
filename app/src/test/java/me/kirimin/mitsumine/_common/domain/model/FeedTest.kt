package me.kirimin.mitsumine._common.domain.model

import me.kirimin.mitsumine._common.network.entity.FeedEntity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FeedTest {

    @Test
    fun convertFromEntityTest() {
        val entity = FeedEntity()
        entity.title = "Testタイトル"
        entity.link = "http://google.com?page=1#section2"
        entity.description = "test"
        entity.contentEncoded = "<blockquote cite=\"https://www.famitsu.com/news/201704/20130678.html\" title=\"『ゼルダの伝説　ブレス オブ ザ ワイルド』全員で遊び、全員で作る。開発環境すら“オープンエア”にした、常識を越えた作品作りに迫る開発者インタビュー【前編】(1/2) - ファミ通.com\"><cite><img src=\"http://cdn-ak.favicon.st-hatena.com/?url=https%3A%2F%2Fwww.famitsu.com%2Fnews%2F201704%2F20130678.html\" alt=\"\" /> <a href=\"https://www.famitsu.com/news/201704/20130678.html\">『ゼルダの伝説　ブレス オブ ザ ワイルド』全員で遊び、全員で作る。開発環境すら“オープンエア”にした、常識を越えた作品作りに迫る開発者インタビュー【前編】(1/2) - ファミ通.com</a></cite><p><a href=\"https://www.famitsu.com/news/201704/20130678.html\"><img src=\"http://cdn-ak.b.st-hatena.com/entryimage/332841340-1492680045.jpg\" alt=\"『ゼルダの伝説　ブレス オブ ザ ワイルド』全員で遊び、全員で作る。開発環境すら“オープンエア”にした、常識を越えた作品作りに迫る開発者インタビュー【前編】(1/2) - ファミ通.com\" title=\"『ゼルダの伝説　ブレス オブ ザ ワイルド』全員で遊び、全員で作る。開発環境すら“オープンエア”にした、常識を越えた作品作りに迫る開発者インタビュー【前編】(1/2) - ファミ通.com\" class=\"entry-image\" /></a></p><p>●この作品、聞けば聞くほど……凄い!! 従来のシリーズから劇的に変化を遂げ、世界から絶賛をもって迎えられた『 ゼルダの伝説　ブレス オブ ザ ワイルド 』（以下、『 ブレス オブ ザ ワイルド 』）。革新性に満ちた遊び、緻密なレベルデザイン、独特で美しいアートワーク、魅惑的なサウンド……など、あらゆる面で称賛されている本作は、あの名作『 ゼルダの伝説 時のオカリナ 』をも越えたシリーズ最高傑作であ...</p><p><a href=\"http://b.hatena.ne.jp/entry/https://www.famitsu.com/news/201704/20130678.html\"><img src=\"http://b.hatena.ne.jp/entry/image/https://www.famitsu.com/news/201704/20130678.html\" alt=\"はてなブックマーク - 『ゼルダの伝説　ブレス オブ ザ ワイルド』全員で遊び、全員で作る。開発環境すら“オープンエア”にした、常識を越えた作品作りに迫る開発者インタビュー【前編】(1/2) - ファミ通.com\" title=\"はてなブックマーク - 『ゼルダの伝説　ブレス オブ ザ ワイルド』全員で遊び、全員で作る。開発環境すら“オープンエア”にした、常識を越えた作品作りに迫る開発者インタビュー【前編】(1/2) - ファミ通.com\" border=\"0\" style=\"border: none\" /></a> <a href=\"http://b.hatena.ne.jp/append?https://www.famitsu.com/news/201704/20130678.html\"><img src=\"http://b.hatena.ne.jp/images/append.gif\" border=\"0\" alt=\"はてなブックマークに追加\" title=\"はてなブックマークに追加\" /></a></p></blockquote><img src=\"http://feeds.feedburner.com/~r/hatena/b/hotentry/~4/N1R_4nmrqmM\" height=\"1\" width=\"1\" alt=\"\"/>"

        val feed = Feed(entity)
        assertThat(feed.title, `is`(entity.title))
        assertThat(feed.thumbnailUrl, `is`("http://cdn-ak.b.st-hatena.com/entryimage/332841340-1492680045.jpg"))
        assertThat(feed.content, `is`("test"))
        assertThat(feed.linkUrl, `is`("http://google.com?page=1%23section2"))
        assertThat(feed.bookmarkCountUrl, `is`("http://b.hatena.ne.jp/entry/image/http%3A%2F%2Fgoogle.com%3Fpage%3D1%2523section2"))
        assertThat(feed.faviconUrl, `is`("http://cdn-ak.favicon.st-hatena.com/?url=http://google.com?page=1%23section2"))
        assertThat(feed.entryLinkUrl, `is`("http://b.hatena.ne.jp/entry/http://google.com?page=1%23section2"))
    }
}