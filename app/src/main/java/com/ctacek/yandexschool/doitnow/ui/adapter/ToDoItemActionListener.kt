package com.ctacek.yandexschool.doitnow.ui.adapter

import com.ctacek.yandexschool.doitnow.data.model.ToDoItem

interface ToDoItemActionListener {
    fun onItemCheck(item: ToDoItem)
    fun onItemDetails(item: ToDoItem)
}