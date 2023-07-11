package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.remote.RemoteDataSourceImpl
import com.ctacek.yandexschool.doitnow.data.datasource.remote.ToDoItemService
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
interface DataSourceModule {
    companion object {
        @Reusable
        @Provides
        fun provideNetworkSource(
            sharedPreferencesHelper: SharedPreferencesAppSettings,
            retrofitService: ToDoItemService
        ): RemoteDataSourceImpl = RemoteDataSourceImpl(sharedPreferencesHelper, retrofitService)

        @Reusable
        @Provides
        fun provideSharedPreferencesDataSource(context: Context) =
            SharedPreferencesAppSettings(context)
    }
}
