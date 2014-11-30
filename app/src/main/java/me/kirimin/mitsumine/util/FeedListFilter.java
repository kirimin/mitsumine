package me.kirimin.mitsumine.util;

import java.util.Iterator;
import java.util.List;

import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.db.NGWordDAO;
import me.kirimin.mitsumine.model.Feed;

public class FeedListFilter {

    public static List<Feed> filter(List<Feed> feedList) {
        List<Feed> blackList = FeedDAO.findAll();
        List<String> ngWordList = NGWordDAO.findAll();
        for (Iterator<Feed> iterator = feedList.iterator(); iterator.hasNext();) {
            Feed feed = iterator.next();
            if (contains(feed, blackList)) {
                iterator.remove();
                continue;
            }
            if (containsWord(feed, ngWordList)) {
                iterator.remove();
                continue;
            }
        }
        return feedList;
    }

    public static boolean contains(Feed feed, List<Feed> list) {
        for (Feed readFeed : list) {
            if (feed.title.equals(readFeed.title)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWord(Feed feed, List<String> ngWordList) {
        for (String ngWord : ngWordList) {
            if (feed.title.indexOf(ngWord) != -1 ||
                    feed.linkUrl.indexOf(ngWord) != -1) {
                return true;
            }
        }
        return false;
    }
}
