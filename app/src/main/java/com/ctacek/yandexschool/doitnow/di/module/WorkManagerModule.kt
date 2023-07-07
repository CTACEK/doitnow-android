package com.ctacek.yandexschool.doitnow.di.module

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.ctacek.yandexschool.doitnow.di.customscope.AppScope
import com.ctacek.yandexschool.doitnow.utils.Constants.REPEAT_INTERVAL
import com.ctacek.yandexschool.doitnow.utils.Constants.WORK_MANAGER_TAG
import com.ctacek.yandexschool.doitnow.utils.PeriodWorkManager
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit


@Module
class WorkManagerModule {

    @AppScope
    @Provides
    fun provideConstraints(): Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    @AppScope
    @Provides
    fun provideWorkManager(
        constraints: Constraints
    ): PeriodicWorkRequest = PeriodicWorkRequest.Builder(
        PeriodWorkManager::class.java,
        REPEAT_INTERVAL,
        TimeUnit.HOURS
    ).setConstraints(constraints)
        .addTag(WORK_MANAGER_TAG)
        .build()
}

