package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.data.datasource.remote.ToDoItemService
import com.ctacek.yandexschool.doitnow.di.AppScope
import com.ctacek.yandexschool.doitnow.utils.Constants.RETROFIT_TIMEOUT
import com.ctacek.yandexschool.doitnow.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
interface NetworkModule {
    companion object {
        @AppScope
        @Provides
        fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        }
        @Reusable
        @Provides
        fun provideTodoApiService(retrofitClient: Retrofit): ToDoItemService {
            return retrofitClient.create(ToDoItemService::class.java)
        }

        @AppScope
        @Provides
        fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

        @AppScope
        @Provides
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }
    }
}
