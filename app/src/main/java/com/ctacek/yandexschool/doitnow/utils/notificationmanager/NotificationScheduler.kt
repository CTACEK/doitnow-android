package com.ctacek.yandexschool.doitnow.utils.notificationmanager

import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem

interface NotificationScheduler {
    fun schedule(item: ToDoItem)
    fun cancel(item: ToDoItem)
    fun cancelAll()
}