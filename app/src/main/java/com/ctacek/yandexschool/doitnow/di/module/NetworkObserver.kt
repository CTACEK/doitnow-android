package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.ctacek.yandexschool.doitnow.di.customscope.AppScope
import com.ctacek.yandexschool.doitnow.utils.internetchecker.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides

@Module
class NetworkObserver {
    @AppScope
    @Provides
    fun provideConnectivityObserver(context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)
}
