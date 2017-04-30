package me.kirimin.mitsumine

import com.activeandroid.ActiveAndroid
import com.activeandroid.Configuration

import android.app.Application
import me.kirimin.mitsumine._common.di.component.AppComponent
import me.kirimin.mitsumine._common.di.component.DaggerAppComponent
import me.kirimin.mitsumine._common.di.module.AppModule
import me.kirimin.mitsumine._common.di.module.InfraModule
import me.kirimin.mitsumine._common.di.module.ViewModule


class MyApplication : Application() {

    private lateinit var applicationComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        // Dagger
        initializeInjector()
        // AA
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

    private fun initializeInjector() {
        applicationComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getApplicationComponent(): AppComponent {
        return applicationComponent
    }
}
