package com.ctacek.yandexschool.doitnow

import android.app.Application
import android.content.Context
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.RetrofitToDoSource
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import com.ctacek.yandexschool.doitnow.utils.InternetConnectionChecker
import com.ctacek.yandexschool.doitnow.utils.ServiceLocator
import com.ctacek.yandexschool.doitnow.utils.locale

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)

        ServiceLocator.register(ToDoItemDatabase.getDatabase(locale()))
        ServiceLocator.register(RetrofitToDoSource().makeRetrofitService())
        ServiceLocator.register(SharedPreferencesAppSettings(locale()))
        ServiceLocator.register(InternetConnectionChecker(locale()))

        ServiceLocator.register(ToDoItemsRepository(locale(), locale(), locale()))
    }
}