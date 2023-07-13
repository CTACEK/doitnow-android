package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.utils.notificationmanager.NotificationScheduler
import com.ctacek.yandexschool.doitnow.utils.notificationmanager.NotificationSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface NotificationSchedulerModule {

    @Reusable
    @Binds
    fun bindNotificationModule(notificationsSchedulerImpl: NotificationSchedulerImpl): NotificationScheduler
}