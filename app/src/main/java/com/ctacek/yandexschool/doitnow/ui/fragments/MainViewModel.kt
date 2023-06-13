package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel() : ViewModel() {
    private val _tasks = MutableLiveData<List<Todoitem>>()
    val tasks: LiveData<List<Todoitem>> = _tasks

    private val _completedTasks = MutableLiveData<Int>()
    val completedTasks: LiveData<Int> = _completedTasks

    private val repository = TodoItemsRepository()


    init {
        viewModelScope.launch {
            _tasks.value = repository.getItems()
            _completedTasks.value = repository.getItems().filter { it.status }.size
        }
    }

    fun updateTask(
        id: String,
        status: Boolean,
    ) {
        repository.editTask(id, status)
        notifyUpdates()
    }

    fun hideCompletedTasks() {
        _tasks.value = repository.getItems().filter { !it.status }
    }

    fun showCompletedTasks() {
        _tasks.value = repository.getItems()
    }

    private fun notifyUpdates() {
        _tasks.postValue(repository.getItems())
        _completedTasks.postValue(repository.getItems().filter { it.status }.size)

    }


}