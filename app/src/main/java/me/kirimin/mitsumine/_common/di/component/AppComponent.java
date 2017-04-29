package me.kirimin.mitsumine._common.di.component;

import javax.inject.Singleton;

import dagger.Component;
import me.kirimin.mitsumine._common.di.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
}