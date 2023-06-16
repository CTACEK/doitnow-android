package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.data.repository.TodoItemsRepository
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<List<ToDoItem>>()
    val tasks: LiveData<List<ToDoItem>> = _tasks

    private val _completedTasks = MutableLiveData<Int>()
    val completedTasks: LiveData<Int> = _completedTasks

    private var visibility = false

    init {
        viewModelScope.launch {
            _tasks.value = repository.getItems().filter { !it.status }
            _completedTasks.value = repository.getItems().filter { it.status }.size
        }
    }

    fun updateStatusTask(id: String, status: Boolean) {
        repository.updateStatusTask(id, status)
        notifyUpdates()
    }

    fun saveTask(newToDoItem: ToDoItem) {
        repository.saveTask(newToDoItem)
    }

    fun createTask(newToDoItem: ToDoItem) {
        repository.createTask(newToDoItem)
    }

    fun loadTask(id: String) : ToDoItem {
        return repository.getTaskById(id)
    }

    fun deleteTask(id: String) {
        repository.deleteTask(id)
        notifyUpdates()
    }

    fun getTasks(isSwitched: Boolean) {
        visibility = isSwitched
        notifyUpdates()
    }

    private fun notifyUpdates() {
        _completedTasks.postValue(repository.getItems().filter { it.status }.size)

        when (visibility) {
            true -> _tasks.postValue(repository.getItems())
            false -> _tasks.postValue(repository.getItems().filter { !it.status })
        }
    }


}