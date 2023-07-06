package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.ctacek.yandexschool.doitnow.utils.internet_checker.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkObserver {
    @Provides
    @Singleton
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}