package me.kirimin.mitsumine._common.di.component;

import javax.inject.Singleton;

import dagger.Component;
import me.kirimin.mitsumine._common.di.module.AppModule;
import me.kirimin.mitsumine._common.di.module.InfraModule;
import me.kirimin.mitsumine._common.di.module.ViewModule;
import me.kirimin.mitsumine.feed.AbstractFeedFragment;
import me.kirimin.mitsumine.top.TopActivity;

@Singleton
@Component(modules = {AppModule.class, InfraModule.class, ViewModule.class})
public interface AppComponent {

    void inject(TopActivity activity);
    void inject(AbstractFeedFragment fragment);
}