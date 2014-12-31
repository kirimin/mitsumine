package me.kirimin.mitsumine.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.kirimin.mitsumine.model.Feed;
import rx.Observable;
import rx.functions.Func1;

public class FeedFunc {

    public static Func1<JSONObject, Observable<Feed>> mapToEntryInfo() {
        return new Func1<JSONObject, Observable<Feed>>() {
            @Override
            public Observable<Feed> call(JSONObject response) {
                try {
                    JSONArray entries = response.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries");
                    List<Feed> feedList = new ArrayList<>();
                    for (int i = 0; i < entries.length(); i++) {
                        feedList.add(parseFeed(entries.getJSONObject(i)));
                    }
                    return Observable.from(feedList);
                } catch (JSONException e) {
                    return null;
                }
            }
        };
    }

    public static Func1<Feed, Boolean> notContains(final List<Feed> readFeedList) {
        return new Func1<Feed, Boolean>() {
            @Override
            public Boolean call(Feed feed) {
                for (Feed readFeed : readFeedList) {
                    if (feed.title.equals(readFeed.title)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Func1<Feed, Boolean> notContainsWord(final List<String> ngWordList) {
        return new Func1<Feed, Boolean>() {
            @Override
            public Boolean call(Feed feed) {
                for (String ngWord : ngWordList) {
                    if (feed.title.contains(ngWord) || feed.linkUrl.contains(ngWord)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private static Feed parseFeed(JSONObject entriesObject) throws JSONException {
        Feed feed = new Feed();
        feed.title = entriesObject.getString("title");
        feed.linkUrl = entriesObject.getString("link");
        feed.content = entriesObject.getString("contentSnippet");
        String content = entriesObject.getString("content");
        feed.thumbnailUrl = parseThumbnailUrl(content);
        feed.bookmarkCountUrl = "http://b.hatena.ne.jp/entry/image/" + feed.linkUrl;
        feed.faviconUrl = "http://cdn-ak.favicon.st-hatena.com/?url=" + feed.linkUrl;
        feed.entryLinkUrl = "http://b.hatena.ne.jp/entry/" + feed.linkUrl;
        return feed;
    }

    private static String parseThumbnailUrl(String content) {
        int urlStartIndex = content.indexOf("http://cdn-ak.b.st-hatena.com/entryimage/");
        if (urlStartIndex != -1) {
            return content.substring(urlStartIndex, content.indexOf("\"", urlStartIndex));
        } else {
            return "";
        }
    }

}
