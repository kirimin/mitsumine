package me.kirimin.mitsumine;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder builder = new Configuration.Builder(getBaseContext());
        builder.setCacheSize(1024 * 1024 * 4);
        builder.setDatabaseName("mitsumine.db");
        builder.setDatabaseVersion(5);
        ActiveAndroid.initialize(builder.create(), true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
