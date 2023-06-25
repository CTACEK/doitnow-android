package com.ctacek.yandexschool.doitnow.ui.adapter

interface ToDoItemActionListener {
    fun onClickCheck(idItem: String, done: Boolean)
    fun onClickItem(idItem: String)
}