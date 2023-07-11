package com.ctacek.yandexschool.doitnow.ui.fragment.main

import androidx.lifecycle.ViewModel
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import com.ctacek.yandexschool.doitnow.utils.internetchecker.ConnectivityObserver
import com.ctacek.yandexschool.doitnow.utils.internetchecker.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val connection: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val visibility: StateFlow<Boolean> = _visibility

    private val _tasks = MutableStateFlow<UiState<List<ToDoItem>>>(UiState.Start)
    val tasks: StateFlow<UiState<List<ToDoItem>>> = _tasks.asStateFlow()


    val countComplete = _tasks.map { state ->
        when (state) {
            is UiState.Success -> {
                state.data.count { it.done }
            }

            else -> {
                0
            }
        }
    }

    init {
        observeNetwork()
        loadData()
    }

    private fun observeNetwork() {
        coroutineScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun changeMode() {
        _visibility.value = _visibility.value.not()
    }


    fun loadData() {
        coroutineScope.launch(Dispatchers.IO) {
            _tasks.emitAll(repository.getAllData())
        }
    }

    fun loadNetworkList() {
        coroutineScope.launch(Dispatchers.IO) {
            _tasks.emitAll(repository.getNetworkTasks())
        }
    }

    fun deleteItem(todoItem: ToDoItem) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoItem)
        }
    }

    fun updateItem(task: ToDoItem) {
        task.changedAt?.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.changeItem(task)
        }
    }
}
