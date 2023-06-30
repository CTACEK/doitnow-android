package com.ctacek.yandexschool.doitnow

import android.app.Application
import android.content.Context
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.RetrofitToDoSource
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import com.ctacek.yandexschool.doitnow.utils.ServiceLocator
import com.ctacek.yandexschool.doitnow.utils.locale
import java.util.concurrent.TimeUnit
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.ctacek.yandexschool.doitnow.utils.PeriodWorkManager
import com.ctacek.yandexschool.doitnow.utils.internet_checker.NetworkConnectivityObserver

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)

        ServiceLocator.register(ToDoItemDatabase.getDatabase(locale()))
        ServiceLocator.register(RetrofitToDoSource().makeRetrofitService())
        ServiceLocator.register(SharedPreferencesAppSettings(locale()))
        ServiceLocator.register(NetworkConnectivityObserver(this))

        ServiceLocator.register(ToDoItemsRepository(locale(), locale(), locale()))
        periodicUpdate()
    }

    private fun periodicUpdate(){
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val myWorkRequest = PeriodicWorkRequest.Builder(
            PeriodWorkManager::class.java,
            8,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag("update_data")
            .build()

        WorkManager.getInstance(this).
        enqueueUniquePeriodicWork("update_data",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest)
    }
}