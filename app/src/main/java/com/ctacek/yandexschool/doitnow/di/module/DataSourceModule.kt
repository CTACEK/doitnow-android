package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.remote.RemoteDataSourceImpl
import com.ctacek.yandexschool.doitnow.data.datasource.remote.ToDoItemService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {


//    @Provides
//    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Provides
    @Singleton
    fun provideNetworkSource(
        sharedPreferencesHelper: SharedPreferencesAppSettings,
        retrofitService: ToDoItemService
    ): RemoteDataSourceImpl = RemoteDataSourceImpl(sharedPreferencesHelper, retrofitService)

    @Singleton
    @Provides
    fun provideSharedPreferencesDataSource(context: Context) = SharedPreferencesAppSettings(context)

}