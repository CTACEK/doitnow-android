package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.ctacek.yandexschool.doitnow.di.AppScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
interface ApplicationModule {
    companion object {
        @AppScope
        @Provides
        fun provideScope(context: Context) = CoroutineScope(SupervisorJob())
    }
}
