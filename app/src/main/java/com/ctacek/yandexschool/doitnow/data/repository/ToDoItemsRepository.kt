package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.NetworkState
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.RetrofitFactory
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiResponse
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemApi
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemResponse
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Repository for todoitems
 *
 * @author Yudov Stanislav
 *
 */
class ToDoItemsRepository(database: ToDoItemDatabase ) {

    private val toDoItemDao = database.toDoItemDao()
    private val remoteData = RetrofitFactory.makeRetrofitService()

    fun getAllToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getToDoItems().map { it -> it.map { it.toToDoItem() } }
    }

    fun getNotCompletedToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getNotCompletedToDoItems().map { it -> it.map { it.toToDoItem() } }
    }

    fun getToDoItemById(id: String): Flow<ToDoItem> {
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

//    suspend fun getRemoteData() : Flow<ToDoItemResponse> {
//        return flow<NetworkState<ToDoItemResponse>> {
//            emit(rem { remoteDataSource.getDog() })
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun getRemoteTasks() : NetworkState<ToDoApiResponse> {
        val response = remoteData.getList()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

}