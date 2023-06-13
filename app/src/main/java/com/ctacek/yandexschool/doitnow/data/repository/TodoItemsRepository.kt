package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.App
import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.model.Todoitem

class TodoItemsRepository() {


    private val randomToDoItems = RandomToDoItems()

    fun getItems(): MutableList<Todoitem> {
        return randomToDoItems.getTasks()
    }

    fun getTaskById(id: String): Todoitem {
        return randomToDoItems.getTaskById(id)
    }

    fun editTask(id: String, status: Boolean) {
        randomToDoItems.editTask(id, status)
    }


}