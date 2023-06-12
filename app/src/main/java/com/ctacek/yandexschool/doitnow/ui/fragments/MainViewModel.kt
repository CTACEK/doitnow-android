package com.ctacek.yandexschool.doitnow.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.datasource.RandomToDoItems
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository
import com.github.javafaker.Dune.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel() : ViewModel() {
    private val date = MutableLiveData<List<Todoitem>>()
    private val completedTasks = MutableLiveData<Int>()
    private val repository = TodoItemsRepository()


    init {
        viewModelScope.launch {
            date.value = repository.getRandomItems()
            completedTasks.value = repository.getRandomItems().filter { it.status }.size
        }
    }

    fun getData() = date
    fun getCountCompleted() = completedTasks

    fun updateTask(
        id: String,
        status: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editTask(id, status)
        }
    }

    fun hideCompletedTasks() {
        date.value = repository.getRandomItems().filter { !it.status }
    }

    fun showCompletedTasks() {
        date.value = repository.getRandomItems()
    }


}