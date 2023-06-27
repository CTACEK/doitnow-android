package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.NetworkState
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiResponseList
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemApi
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemResponseRequest
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList

/**
 * Repository for todoitems
 *
 * @author Yudov Stanislav
 *
 */
class ToDoItemsRepository(
    localDataSource: ToDoItemDatabase,
    private val remoteDataSource: ToDoItemApi,
    private val sharedPreferences: SharedPreferencesAppSettings
) {

    private val toDoItemDao = localDataSource.toDoItemDao()

    fun getAllToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getToDoItems().map { it -> it.map { it.toToDoItem() } }
    }

    fun getToDoItemById(id: String): Flow<ToDoItem> {
        return toDoItemDao.getToDoItemById(id = id).map { it.toToDoItem() }
    }

    suspend fun updateStatusToDoItem(id: String, done: Boolean, time: Long) {
        return toDoItemDao.updateDone(id, done, time)
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

    suspend fun deleteAllItems() {
        toDoItemDao.deleteAllToDoItems()
    }

//    suspend fun getRemoteData() : Flow<ToDoItemResponseRequest> {
//        return flow<NetworkState<ToDoItemResponseRequest>> {
//            emit(rem { remoteDataSource.getDog() })
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun makeMerge(newList: List<ToDoItem>) {
        toDoItemDao.mergeToDoItems(newList.map { ToDoItemEntity.fromToDoTask(it) })
    }

//    suspend fun transformFlowToList(flow: Flow<List<ToDoItem>>): List<ToDoItem> {
//        val result = mutableListOf<ToDoItem>()
//        flow.collect { list ->
//            result.addAll(list)
//        }
//        return result
//    }

    suspend fun updateRemoteList(newList: List<ToDoItem>) {
//      remoteDataSource.updateList()
    }

    suspend fun updateRemoteTasks(): NetworkState<ToDoApiResponseList> {
        val list: List<ToDoItem> = toDoItemDao.getToDoItemsNoFlow().map { it.toToDoItem() }

        val response = remoteDataSource.updateList(
            lastKnownRevision = 8,
            ToDoApiRequestList(status = "ok", list.map { ToDoItemResponseRequest.fromToDoTask(it) })
        )

        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                toDoItemDao.mergeToDoItems(responseBody.list.map { ToDoItemEntity.fromToDoTask(it.toToDoItem()) })
                return NetworkState.Success(responseBody)
            } else {
                return NetworkState.Error(response)
            }
        } else {
            return NetworkState.Error(response)
        }
    }

    suspend fun getRemoteTasks(): NetworkState<ToDoApiResponseList> {
        val response = remoteDataSource.getList()
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

    suspend fun get(): NetworkState<ToDoApiResponseList> {
        val response = remoteDataSource.getList()
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