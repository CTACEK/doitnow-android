package com.ctacek.yandexschool.doitnow.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ctacek.yandexschool.doitnow.data.model.LoadingState
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import kotlinx.coroutines.runBlocking

class PeriodWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val repository: ToDoItemsRepository by localeLazy()

    override fun doWork(): Result {
        return when (mergeData()) {
            is LoadingState.Success<*> -> Result.success()
            else -> {
                Result.failure()
            }
        }
    }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getRemoteTasks()
    }

}