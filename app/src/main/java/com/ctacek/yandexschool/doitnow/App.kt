package com.ctacek.yandexschool.doitnow

import android.app.Application
import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository

class App : Application() {
    var repository = TodoItemsRepository()
}