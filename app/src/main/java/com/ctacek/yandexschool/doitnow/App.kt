package com.ctacek.yandexschool.doitnow

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.ctacek.yandexschool.doitnow.di.AppComponent
import com.ctacek.yandexschool.doitnow.di.DaggerAppComponent
import com.ctacek.yandexschool.doitnow.utils.Constants.WORK_MANAGER_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings

class App : Application() {

    @Inject
    lateinit var myWorkRequest: PeriodicWorkRequest

    @Inject
    lateinit var sharedPreferencesAppSettings: SharedPreferencesAppSettings

    @Inject
    lateinit var coroutineScope: CoroutineScope

    lateinit var appComponent: AppComponent

    override fun onCreate() {

        super.onCreate()

        appComponent = DaggerAppComponent.factory()
            .create(context = applicationContext)

        appComponent.injectApplication(this)
        sharedPreferencesAppSettings.putThemeMode(sharedPreferencesAppSettings.getThemeMode())

        periodicUpdate()
    }

    override fun onTerminate() {
        super.onTerminate()
        coroutineScope.cancel()
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
