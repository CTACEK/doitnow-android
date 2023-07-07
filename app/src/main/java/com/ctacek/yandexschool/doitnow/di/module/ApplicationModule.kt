package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideScope(context: Context) = CoroutineScope(SupervisorJob())
}