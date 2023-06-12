package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.model.Todoitem

class TodoItemsRepository () {

    private val randomToDoItems = RandomToDoItems()

    fun getRandomItems(): MutableList<Todoitem> {
        return randomToDoItems.getToDoItems()
    }

    fun editTask(id: String, status: Boolean){
        randomToDoItems.editTaks(id, status)
    }

}