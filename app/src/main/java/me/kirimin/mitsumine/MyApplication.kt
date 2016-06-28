package me.kirimin.mitsumine

import com.activeandroid.ActiveAndroid
import com.activeandroid.Configuration

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val builder = Configuration.Builder(baseContext)
        builder.setCacheSize(1024 * 1024 * 4)
        builder.setDatabaseName("mitsumine.db")
        builder.setDatabaseVersion(6)
        ActiveAndroid.initialize(builder.create(), true)
    }

    override fun onTerminate() {
        super.onTerminate()
        ActiveAndroid.dispose()
    }
}
