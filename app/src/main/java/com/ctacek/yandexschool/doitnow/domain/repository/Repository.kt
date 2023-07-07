package com.ctacek.yandexschool.doitnow.domain.repository

import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllData(): Flow<UiState<List<ToDoItem>>>
    fun getItem(itemId: String): ToDoItem
    suspend fun addItem(todoItem: ToDoItem)
    suspend fun deleteItem(todoItem: ToDoItem)
    suspend fun changeItem(todoItem: ToDoItem)
    fun getNetworkTasks(): Flow<UiState<List<ToDoItem>>>
    suspend fun deleteCurrentItems()
}