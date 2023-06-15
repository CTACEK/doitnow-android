package com.ctacek.yandexschool.doitnow.data.repository

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

    fun updateStatusTask(id: String, status: Boolean) {
        randomToDoItems.updateStatusTask(id, status)
    }

    fun saveTask(newToDoItem: Todoitem){
        randomToDoItems.saveTask(newToDoItem)
    }

    fun deleteTask(id: String) {
        randomToDoItems.deleteTask(id)
    }

    fun createTask(newToDoItem: Todoitem){
        randomToDoItems.createTask(newToDoItem)
    }

}