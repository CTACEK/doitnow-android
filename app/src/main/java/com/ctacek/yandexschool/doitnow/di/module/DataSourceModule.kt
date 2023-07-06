package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {

//    @Binds
//    fun bindRemoteDataSource(todoRemoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

//    @Provides
//    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Singleton
    @Provides
    fun provideSharedPreferencesDataSource(context: Context) = SharedPreferencesAppSettings(context)

}