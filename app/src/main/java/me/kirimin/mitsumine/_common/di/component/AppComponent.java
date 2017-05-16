package me.kirimin.mitsumine._common.di.component;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;
import me.kirimin.mitsumine._common.di.module.AppModule;
import me.kirimin.mitsumine._common.di.module.InfraModule;
import me.kirimin.mitsumine._common.di.module.ViewModule;
import me.kirimin.mitsumine._common.ui.BaseActivity;
import me.kirimin.mitsumine._common.ui.BaseFragment;
import me.kirimin.mitsumine.about.AboutActivity;
import me.kirimin.mitsumine.about.LicenseActivity;
import me.kirimin.mitsumine.bookmarklist.BookmarkListFragment;
import me.kirimin.mitsumine.entryinfo.EntryInfoActivity;
import me.kirimin.mitsumine.feed.AbstractFeedFragment;
import me.kirimin.mitsumine.feed.keyword.KeywordSearchActivity;
import me.kirimin.mitsumine.feed.read.ReadActivity;
import me.kirimin.mitsumine.feed.readlater.ReadLaterActivity;
import me.kirimin.mitsumine.feed.user.UserSearchActivity;
import me.kirimin.mitsumine.login.LoginActivity;
import me.kirimin.mitsumine.mybookmark.MyBookmarkSearchActivity;
import me.kirimin.mitsumine.mybookmark.MyBookmarkSearchFragment;
import me.kirimin.mitsumine.registerbookmark.RegisterBookmarkFragment;
import me.kirimin.mitsumine.setting.SettingActivity;
import me.kirimin.mitsumine.top.TopActivity;

@Singleton
@Component(modules = {AppModule.class, InfraModule.class, ViewModule.class})
public interface AppComponent {

    void inject(TopActivity activity);
    void inject(ReadActivity activity);
    void inject(ReadLaterActivity activity);
    void inject(KeywordSearchActivity activity);
    void inject(UserSearchActivity activity);
    void inject(MyBookmarkSearchActivity activity);
    void inject(SettingActivity activity);
    void inject(AboutActivity activity);
    void inject(LicenseActivity activity);
    void inject(EntryInfoActivity activity);
    void inject(LoginActivity activity);
    void inject(AbstractFeedFragment fragment);
    void inject(MyBookmarkSearchFragment fragment);
    void inject(BookmarkListFragment fragment);
    void inject(RegisterBookmarkFragment fragment);
}