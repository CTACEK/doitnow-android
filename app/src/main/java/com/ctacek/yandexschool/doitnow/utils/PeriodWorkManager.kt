package com.ctacek.yandexschool.doitnow.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PeriodWorkManager(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

//    private val repository: ToDoItemsRepositoryImpl

    override fun doWork(): Result {
//        return when (mergeData()) {
//            is LoadingState.Success<*> -> Result.success()
//            else -> {
//                Result.failure()
//            }
//        }
        return Result.success()
    }

//    private fun mergeData() = runBlocking {
//        return@runBlocking repository.getRemoteTasks()
//    }

}