package me.kirimin.mitsumine.view;

import me.kirimin.mitsumine.data.network.api.FeedApi;

public interface TopView {

    void closeNavigation();

    void openNavigation();

    boolean isOpenNavigation();

    void refreshShowCategoryAndType(FeedApi.TYPE type);

    void startActivity(Class<?> activityClass);
}
