package me.kirimin.mitsumine.ui.fragment;

import me.kirimin.mitsumine.db.FeedDAO;
import me.kirimin.mitsumine.model.Feed;

public class ReadFeedFragment extends AbstractFeedFragment {

    @Override
    void requestFeed() {
        setFeed(FeedDAO.findByType(Feed.TYPE_READ));
    }

    @Override
    boolean isUseReadLater() {
        return true;
    }

    @Override
    boolean isUseRead() {
        return false;
    }
}
