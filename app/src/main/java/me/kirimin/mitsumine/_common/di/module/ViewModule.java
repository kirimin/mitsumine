package me.kirimin.mitsumine._common.di.module;

import dagger.Module;
import dagger.Provides;
import me.kirimin.mitsumine.feed.FeedPresenter;
import me.kirimin.mitsumine.feed.FeedUseCase;
import me.kirimin.mitsumine.top.TopPresenter;
import me.kirimin.mitsumine.top.TopUseCase;

@Module
public class ViewModule {

    @Provides
    TopPresenter provideTopPresenter(TopUseCase useCase) {
        return new TopPresenter(useCase);
    }

    @Provides
    FeedPresenter provideFeedPresenter(FeedUseCase useCase) {
        return new FeedPresenter(useCase);
    }
}
