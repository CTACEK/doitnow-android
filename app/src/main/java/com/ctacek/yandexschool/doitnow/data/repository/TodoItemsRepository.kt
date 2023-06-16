package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem

class TodoItemsRepository {


    private val randomToDoItems = RandomToDoItems()

    fun getItems(): List<ToDoItem> {
        return ArrayList(randomToDoItems.getTasks().values)
    }

    fun getTaskById(id: String): ToDoItem {
        return randomToDoItems.getTaskById(id.toInt())
    }

    fun updateStatusTask(id: String, status: Boolean) {
        randomToDoItems.updateStatusTask(id, status)
    }

    fun saveTask(newToDoItem: ToDoItem) {
        randomToDoItems.saveTask(newToDoItem)
    }

    fun deleteTask(id: String) {
        randomToDoItems.deleteTask(id.toInt())
    }

    fun createTask(newToDoItem: ToDoItem) {
        randomToDoItems.createTask(newToDoItem)
    }

}