package com.ctacek.yandexschool.doitnow.ui.adapter

import com.ctacek.yandexschool.doitnow.data.model.ToDoItem

interface ToDoItemActionListener {
    fun onClickCheck(item: ToDoItem)
    fun onClickItem(idItem: String)
}