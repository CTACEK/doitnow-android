package com.ctacek.yandexschool.doitnow.ui.adapter

import com.ctacek.yandexschool.doitnow.data.model.Todoitem

interface ToDoItemActionListener {
    fun onItemCheck(item: Todoitem)
    fun onItemDetails(item: Todoitem)
}