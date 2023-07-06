package com.ctacek.yandexschool.doitnow.domain.repository

import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllToDoItems(): Flow<UiState<List<ToDoItem>>>
    fun getToDoItemById(itemId: String): ToDoItem
    suspend fun addItem(todoItem: ToDoItem)
    suspend fun deleteItem(todoItem: ToDoItem)
    suspend fun changeItem(todoItem: ToDoItem)
    fun getNetworkTasks(): Flow<UiState<List<ToDoItem>>>

    suspend fun postNetworkItem(newItem: ToDoItem)
    suspend fun deleteNetworkItem(id: String)
    suspend fun updateNetworkItem(item: ToDoItem)
    suspend fun deleteAll()
}