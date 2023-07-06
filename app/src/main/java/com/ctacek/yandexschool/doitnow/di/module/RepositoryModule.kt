package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.remote.ToDoItemService
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: ToDoItemDatabase,
        remoteDataSource: ToDoItemService,
        sharedPreferences: SharedPreferencesAppSettings
    ): ToDoItemsRepositoryImpl =
        ToDoItemsRepositoryImpl(localDataSource, remoteDataSource, sharedPreferences)
}