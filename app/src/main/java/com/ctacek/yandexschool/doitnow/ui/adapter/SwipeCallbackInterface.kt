package com.ctacek.yandexschool.doitnow.ui.adapter

import com.ctacek.yandexschool.doitnow.data.model.ToDoItem

interface SwipeCallbackInterface {
    fun onDelete(todoItem: ToDoItem)
    fun onChangeDone(todoItem: ToDoItem)
}