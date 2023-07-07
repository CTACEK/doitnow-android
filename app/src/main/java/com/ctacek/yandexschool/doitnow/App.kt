package com.ctacek.yandexschool.doitnow

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.ctacek.yandexschool.doitnow.di.AppComponent
import com.ctacek.yandexschool.doitnow.di.DaggerAppComponent
import com.ctacek.yandexschool.doitnow.utils.Constants.WORK_MANAGER_TAG
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var myWorkRequest: PeriodicWorkRequest

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory()
            .create(context = applicationContext)

        appComponent.injectApplication(this)

        periodicUpdate()
    }

    private fun periodicUpdate() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
    }
}


val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
