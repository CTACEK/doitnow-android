package com.ctacek.yandexschool.doitnow

import android.app.Application
import android.content.Context
import com.ctacek.yandexschool.doitnow.di.AppComponent
import com.ctacek.yandexschool.doitnow.di.DaggerAppComponent
import com.ctacek.yandexschool.doitnow.utils.internet_checker.NetworkConnectivityObserver
import javax.inject.Inject

class App : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .context(context = applicationContext)
            .build()

//        appComponent.injectApplication(this)


//        ServiceLocator.register<Context>(this)

//        ServiceLocator.register(ToDoItemDatabase.getDatabase(locale()))
//        ServiceLocator.register(RetrofitToDoClient().makeRetrofitService())
//        ServiceLocator.register(SharedPreferencesAppSettings(locale()))
//        ServiceLocator.register(NetworkConnectivityObserver(this))
//        ServiceLocator.register(ToDoItemsRepositoryImpl(locale(), locale(), locale()))
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }