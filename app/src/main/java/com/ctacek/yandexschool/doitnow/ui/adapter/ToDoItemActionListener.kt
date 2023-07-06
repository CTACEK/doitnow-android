package com.ctacek.yandexschool.doitnow.ui.adapter

import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem


interface ToDoItemActionListener {
    fun onClickCheck(item: ToDoItem)
    fun onClickItem(idItem: String)
}