package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.NetworkState
import com.ctacek.yandexschool.doitnow.data.model.LoadingState
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import com.ctacek.yandexschool.doitnow.utils.InternetConnectionChecker
import com.ctacek.yandexschool.doitnow.utils.internet_checker.ConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date


class MainViewModel(
    private val repository: ToDoItemsRepository,
    private val internetChecker: InternetConnectionChecker
) : ViewModel() {

    private var job: Job? = null

    var modeAll: Boolean = false

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _tasks = MutableSharedFlow<List<ToDoItem>>()
    val tasks: SharedFlow<List<ToDoItem>> = _tasks.asSharedFlow()

    val countCompletedTask: Flow<Int> = _tasks.map { it -> it.count { it.done } }

    private val _oneTask = MutableSharedFlow<ToDoItem>()
    val oneTask: SharedFlow<ToDoItem> = _oneTask.asSharedFlow()

    private val _loadingState =
        MutableStateFlow<LoadingState<Any>>(LoadingState.Success("Loaded from rood complete!"))
    val loadingState: StateFlow<LoadingState<Any>> = _loadingState.asStateFlow()

    init {
        if (internetChecker.hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = repository.getRemoteTasks()) {
                    is NetworkState.Success -> repository.makeMerge(response.data.list.map { it.toToDoItem() })
                    is NetworkState.Error -> println("Internet error ${response.response.code()}")
                }
            }
        }

        loadData()
    }



    fun changeDone() {
        modeAll = !modeAll
        job?.cancel()
        loadData()
    }

    fun loadData() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _tasks.emitAll(repository.getAllToDoItems())
        }
    }

    fun createTask(todoItem: ToDoItem) {
        if (internetChecker.hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = repository.createRemoteOneTask(todoItem)) {
                    is NetworkState.Success -> println("Good create")
                    is NetworkState.Error -> println("Internet error ${response.response.code()}")
                }
            }
        } else {
//            _loadingState.value = LoadingState.Error("No internet(")
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.createItem(todoItem)
        }
    }

    fun deleteTask(todoItem: ToDoItem) {
        if (internetChecker.hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = repository.deleteRemoteOneTask(todoItem)) {
                    is NetworkState.Success -> println("Good delete")
                    is NetworkState.Error -> println("Internet error ${response.response.code()}")
                }
            }
        } else {
//            _loadingState.value = LoadingState.Error("No internet(")
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToDoItem(todoItem)
        }
    }

    fun updateTask(newItem: ToDoItem) {
        if (internetChecker.hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = repository.updateRemoteOneTask(newItem)) {
                    is NetworkState.Success -> println("Good update")
                    is NetworkState.Error -> println("Internet error ${response.response.code()}")
                }
            }
        } else {
//            _loadingState.value = LoadingState.Error("No internet(")
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateToDoItem(newItem)
        }
    }

    fun changeTaskDone(id: String, done: Boolean) {
        if (internetChecker.hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO) {
                val task = repository.getToDoItemById(id)

                when (val response = repository.updateRemoteOneTask(task)) {
                    is NetworkState.Success -> println("Good update")
                    is NetworkState.Error -> println("Internet error ${response.response.code()}")
                }
            }
        } else {
//            _loadingState.value = LoadingState.Error("No internet(")
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStatusToDoItem(id, done, Date().time)
        }
    }

    fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _oneTask.emit(repository.getToDoItemById(id))
        }
    }

    fun startPatch() {
        if (internetChecker.hasInternetConnection()) {
            viewModelScope.launch(Dispatchers.IO) {
                _loadingState.value = LoadingState.Loading(true)
                when (val response = repository.updateRemoteTasks()) {
                    is NetworkState.Success -> _loadingState.value =
                        LoadingState.Success("Good patch")

                    is NetworkState.Error -> _loadingState.value =
                        LoadingState.Error("Internet error ${response.response.code()}")
                }
            }
        } else {
            _loadingState.value = LoadingState.Error("No internet")
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}