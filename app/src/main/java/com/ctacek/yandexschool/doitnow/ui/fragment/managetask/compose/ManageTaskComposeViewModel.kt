package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose

import androidx.lifecycle.ViewModel
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.eventsholders.ManageAction
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.compose.eventsholders.ManageUiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class ManageTaskComposeViewModel @Inject constructor(
    private val repository: ToDoItemsRepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _actions: Channel<ManageAction> = Channel(Channel.BUFFERED)
    val actions: Flow<ManageAction> = _actions.receiveAsFlow()


    private val _todoItem: MutableStateFlow<ToDoItem> = MutableStateFlow(ToDoItem())
    val todoItem = _todoItem.asStateFlow()

    fun getItem(id: String) {
        if (_todoItem.value.id == "-1") {
            coroutineScope.launch(Dispatchers.IO) {
                _todoItem.emit(repository.getItem(id))
            }
        }
    }

    fun setItem() {
        _todoItem.value = ToDoItem()
    }

    private fun addItem(todoItem: ToDoItem) {
        todoItem.id = UUID.randomUUID().toString()
        todoItem.changedAt = Date(System.currentTimeMillis())

        coroutineScope.launch(Dispatchers.IO) {
            repository.addItem(todoItem.copy())
        }

        _actions.trySend(ManageAction.NavigateBack)
    }

    private fun deleteItem(todoItem: ToDoItem) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoItem)
        }
        _actions.trySend(ManageAction.NavigateBack)
    }

    private fun updateItem(task: ToDoItem) {
        task.changedAt?.time = System.currentTimeMillis()

        coroutineScope.launch(Dispatchers.IO) {
            repository.changeItem(task)
        }
        _actions.trySend(ManageAction.NavigateBack)
    }

    fun handleEvent(event: ManageUiEvent) {
        when (event) {
            is ManageUiEvent.ChangeDescription -> changeTitle(event.text)
            is ManageUiEvent.ChangeImportance -> changeImportance(event.importance)
            is ManageUiEvent.ChangeDeadline -> changeDeadline(event.deadline)
            ManageUiEvent.SaveTask -> {
            if (_todoItem.value.id == "-1") {
                addItem(_todoItem.value)
                _actions.trySend(ManageAction.NavigateBack)
            } else {
                updateItem(_todoItem.value)
                _actions.trySend(ManageAction.NavigateBack)
            }
        }
            ManageUiEvent.DeleteTodo -> deleteItem(_todoItem.value)
            ManageUiEvent.ClearDeadline -> changeDeadline(null)
            ManageUiEvent.Close -> _actions.trySend(ManageAction.NavigateBack)
        }
    }

    private fun changeTitle(text: String) {
        _todoItem.update { it.copy(description = text) }
    }

    private fun changeImportance(important: Importance) {
        _todoItem.update { it.copy(importance = important) }
    }

    private fun changeDeadline(deadline: Date?) {
        _todoItem.update { it.copy(deadline = deadline) }
    }
}
