package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for todoitems
 *
 * @author Yudov Stanislav
 *
 */
class ToDoItemsRepository(database: ToDoItemDatabase) {

    private val toDoItemDao = database.toDoItemDao()

    suspend fun getAllToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getToDoItems().map { it -> it.map { it.toToDoItem() } }
    }

    suspend fun getNotCompletedToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getNotCompletedToDoItems().map { it -> it.map { it.toToDoItem() } }
    }

    suspend fun getToDoItemById(id: String): Flow<ToDoItem> {
        return toDoItemDao.getToDoItemById(id = id).map { it.toToDoItem() }
    }

    suspend fun updateStatusToDoItem(id: String, done: Boolean) {
        return toDoItemDao.updateDone(id, done)
    }

    suspend fun updateToDoItem(toDoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(toDoItem)
        return toDoItemDao.updateToDoItem(toDoItemEntity)
    }

    suspend fun createItem(toDoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(toDoItem)
        return toDoItemDao.createItem(toDoItemEntity)
    }

    suspend fun deleteToDoItem(toDoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(toDoItem)
        return toDoItemDao.deleteToDoItem(toDoItemEntity)
    }

}