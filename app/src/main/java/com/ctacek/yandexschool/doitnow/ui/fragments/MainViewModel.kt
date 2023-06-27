package com.ctacek.yandexschool.doitnow.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.NetworkState
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.util.Date


class MainViewModel(
    private val repository: ToDoItemsRepository
) : ViewModel() {

    private var job: Job? = null

    var modeAll: Boolean = false

    private val _tasks = MutableSharedFlow<List<ToDoItem>>()
    val tasks: SharedFlow<List<ToDoItem>> = _tasks.asSharedFlow()


    val countCompletedTask: Flow<Int> = _tasks.map { it -> it.count { it.done } }

    private val _oneTask = MutableSharedFlow<ToDoItem>()
    val oneTask: SharedFlow<ToDoItem> = _oneTask.asSharedFlow()

    init {
        loadData()
    }

    fun changeDone() {
        modeAll = !modeAll
        job?.cancel()
        loadData()
    }

    private fun loadData() {
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

    fun changeTaskDone(id: String, done: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStatusToDoItem(id, done, Date().time)
        }
    }


    fun loadTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _oneTask.emitAll(repository.getToDoItemById(id))
        }
    }

    fun loadRemoteTask() {
        viewModelScope.launch(Dispatchers.IO) {
//            when (val response = repository.getRemoteTasks()){
//                is NetworkState.Success -> _tasks.emit(response.data.list.map { it.toToDoItem() })
//                is NetworkState.Error -> {
//                    // Do something
//                }
//            }
//            repository.getAllToDoItems().flatMapLatest { it }
        }
    }

    fun startPatch() {
        viewModelScope.launch(Dispatchers.IO) {

            when (val response = repository.updateRemoteTasks()) {
                is NetworkState.Success -> {
                    //
                }
                is NetworkState.Error -> {
                    // Do something
                }

                is NetworkState.Loading -> {
                    // Do something
                }
            }
        }
    }

//    fun startMerge(){
//        viewModelScope.launch(Dispatchers.IO) {
//            when (val response = repository.getRemoteTasks()){
//                is NetworkState.Success -> {
//                    repository.makeMerge(response.data.list.map { it.toToDoItem() })
//                    getData()
//                }
//                is NetworkState.Error -> {
//                    // Do something
//                }
//                is NetworkState.Loading -> {
//                    // Do something
//                }
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    //    private fun <T> Flow<T>.asLiveDataFlow() =
//        shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)


}