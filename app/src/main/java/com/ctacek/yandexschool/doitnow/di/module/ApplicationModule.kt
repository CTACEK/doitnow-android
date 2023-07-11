package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.di.customscope.AppScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
interface ApplicationModule {
    companion object {
        @AppScope
        @Provides
        fun provideScope() = CoroutineScope(SupervisorJob())
    }
}
