package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.model.LoadingState
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver
import com.ctacek.yandexschool.doitnow.utils.internet_checker.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: ToDoItemsRepository,
    private val connection: NetworkConnectivityObserver
) : ViewModel() {

    private var job: Job? = null

    var showAll: Boolean = false

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _tasks = MutableSharedFlow<List<ToDoItem>>()
    val tasks: SharedFlow<List<ToDoItem>> = _tasks.asSharedFlow()

    val countCompletedTask: Flow<Int> = _tasks.map { it -> it.count { it.done } }

    private val _loadingState =
        MutableStateFlow<LoadingState<Any>>(LoadingState.Success("Loaded from rood complete!"))
    val loadingState: StateFlow<LoadingState<Any>> = _loadingState.asStateFlow()

    private var _currentItem = MutableStateFlow(ToDoItem())
    var currentItem = _currentItem.asStateFlow()

    init {
        observeNetwork()
        loadLocalData()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun changeDone() {
        showAll = !showAll
        job?.cancel()
        loadLocalData()
    }

    fun loadLocalData() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _tasks.emitAll(repository.getAllToDoItems())
        }
    }

    fun createTask(todoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createItem(todoItem)
        }
    }

    fun deleteTask(todoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToDoItem(todoItem)
        }
    }

    fun updateTask(newItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateToDoItem(newItem)
        }
    }

    fun changeTaskDone(task: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStatusToDoItem(task.id, !task.done)
        }
    }

    fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentItem.emit(repository.getToDoItemById(id))
        }
    }

    fun loadRemoteList() {
        if (status.value == ConnectivityObserver.Status.Available) {
            _loadingState.value = LoadingState.Loading(true)
            viewModelScope.launch(Dispatchers.IO) {
                _loadingState.emit(repository.getRemoteTasks())
            }
        }
    }

    fun createRemoteTask(todoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createRemoteTask(todoItem)
        }
    }

    fun clearTask() {
        _currentItem.value = ToDoItem()
    }

    fun deleteRemoteTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRemoteTask(id)
        }
    }

    fun updateRemoteTask(todoItem: ToDoItem) {
        val item = todoItem.copy(done = !todoItem.done)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRemoteTask(item)
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}