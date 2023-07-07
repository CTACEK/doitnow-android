package com.ctacek.yandexschool.doitnow.ui.adapter.swipe

import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem

interface SwipeCallbackInterface {
    fun onDelete(todoItem: ToDoItem)
    fun onChangeDone(todoItem: ToDoItem)
}
