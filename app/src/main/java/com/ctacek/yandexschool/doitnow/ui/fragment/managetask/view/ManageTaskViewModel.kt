package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.view

import androidx.lifecycle.ViewModel
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ManageTaskViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {


    private val _todoItem:MutableStateFlow<UiState<ToDoItem>> = MutableStateFlow(UiState.Start)
    val todoItem = _todoItem.asStateFlow()

    fun getItem(id:String) {
        if(todoItem.value == UiState.Start) {
            coroutineScope.launch(Dispatchers.IO) {
                _todoItem.emit(UiState.Success(repository.getItem(id)))
            }
        }
    }

    fun setItem() {
        if(todoItem.value == UiState.Start) {
            _todoItem.value = UiState.Success(ToDoItem())
        }
    }

    fun addItem(todoItem: ToDoItem){
        coroutineScope.launch(Dispatchers.IO) {
            repository.addItem(todoItem.copy())
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
