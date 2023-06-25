package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: ToDoItemsRepository
) : ViewModel() {

    private val _completedTasks = MutableLiveData<Int>()
    val completedTasks: LiveData<Int> = _completedTasks


    var job: Job? = null

    var modeAll: Boolean = false

    val tasks = MutableSharedFlow<List<ToDoItem>>()

    val item = MutableSharedFlow<ToDoItem>()
    fun changeMode() {
        job?.cancel()
        modeAll = !modeAll
        getData()
    }

    fun getData() {
        when (modeAll) {
            true -> loadAllData()
            false -> loadNotCompletedTasks()
        }
    }

    private fun loadNotCompletedTasks() {
        job = viewModelScope.launch {
            tasks.emitAll(repository.getNotCompletedToDoItems().asLiveDataFlow())
        }
    }

    private fun loadAllData() {
        job = viewModelScope.launch {
            tasks.emitAll(repository.getAllToDoItems().asLiveDataFlow())
        }
    }

    fun createTask(todoItem: ToDoItem) {
        viewModelScope.launch {
            repository.createItem(todoItem)
        }
    }

    fun deleteTask(todoItem: ToDoItem) {
        viewModelScope.launch {
            repository.deleteToDoItem(todoItem)
        }
    }

    fun updateTask(newItem: ToDoItem) {
        viewModelScope.launch {
            repository.updateToDoItem(newItem)
        }
    }

    fun changeTaskDone(id: String, done: Boolean) {
        viewModelScope.launch {
            repository.updateStatusToDoItem(id, done)
        }
    }


    fun loadTask(id: String) {
        viewModelScope.launch {
            item.emitAll(repository.getToDoItemById(id).asLiveDataFlow())
        }
    }


    private fun <T> Flow<T>.asLiveDataFlow() =
        shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)


}