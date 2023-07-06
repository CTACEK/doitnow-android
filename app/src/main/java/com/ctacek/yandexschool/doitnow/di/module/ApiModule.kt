package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.data.datasource.remote.ToDoItemService
import com.ctacek.yandexschool.doitnow.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object ApiModule {
    @Singleton
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    }

    @Provides
    fun provideTodoApiService(retrofitClient: Retrofit): ToDoItemService {
        return retrofitClient.create(ToDoItemService::class.java)
    }
}