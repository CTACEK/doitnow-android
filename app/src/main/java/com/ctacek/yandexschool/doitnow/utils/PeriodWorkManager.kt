package com.ctacek.yandexschool.doitnow.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ctacek.yandexschool.doitnow.App
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PeriodWorkManager(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    @Inject
    lateinit var repository: ToDoItemsRepositoryImpl

    override fun doWork(): Result {
        (context.applicationContext as App).appComponent.injectWorkManager(this)
        mergeData()
        return Result.success()
    }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getNetworkTasks()
    }

}
