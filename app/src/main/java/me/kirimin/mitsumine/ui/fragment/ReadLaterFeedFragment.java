package me.kirimin.mitsumine.ui.fragment;

import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.model.Feed;

public class ReadLaterFeedFragment extends AbstractFeedFragment {

    @Override
    void requestFeed() {
        setFeed(FeedDAO.findByType(Feed.TYPE_READ_LATER));
        dismissRefreshing();
    }

    @Override
    boolean isUseReadLater() {
        return false;
    }

    @Override
    boolean isUseRead() {
        return true;
    }
}
