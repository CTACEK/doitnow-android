package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.NetworkState
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestElement
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiResponseElement
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiResponseList
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemApi
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemResponseRequest
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    suspend fun getToDoItemById(id: String): ToDoItem {
        return toDoItemDao.getToDoItemById(id = id).toToDoItem()
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

    suspend fun makeMerge(newList: List<ToDoItem>) {
        toDoItemDao.mergeToDoItems(newList.map { ToDoItemEntity.fromToDoTask(it) })
    }

    suspend fun updateRemoteOneTask(toDoTask: ToDoItem): NetworkState<ToDoApiResponseElement> {
        val response = remoteDataSource.updateTask(
            lastKnownRevision = sharedPreferences.getRevisionId(),
            itemId = toDoTask.id,
            ToDoApiRequestElement(ToDoItemResponseRequest.fromToDoTask(toDoTask, sharedPreferences.getDeviceId()?: "null"))
        )

        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                sharedPreferences.putRevisionId(responseBody.revision)
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun deleteRemoteOneTask(toDoTask: ToDoItem): NetworkState<ToDoApiResponseElement> {
        val response = remoteDataSource.deleteTask(
            lastKnownRevision = sharedPreferences.getRevisionId(),
            itemId = toDoTask.id
        )

        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                sharedPreferences.putRevisionId(responseBody.revision)
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun createRemoteOneTask(newTask: ToDoItem): NetworkState<ToDoApiResponseElement> {
        val response = remoteDataSource.addTask(
            lastKnownRevision = sharedPreferences.getRevisionId(),
            ToDoApiRequestElement(ToDoItemResponseRequest.fromToDoTask(newTask, sharedPreferences.getDeviceId()?: "null"))
        )

        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                sharedPreferences.putRevisionId(responseBody.revision)
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun updateRemoteTasks(): NetworkState<ToDoApiResponseList> {
        val list: List<ToDoItem> = toDoItemDao.getToDoItemsNoFlow().map { it.toToDoItem() }

        val response = remoteDataSource.updateList(
            lastKnownRevision = sharedPreferences.getRevisionId(),
            ToDoApiRequestList(status = "ok", list.map { ToDoItemResponseRequest.fromToDoTask(it, sharedPreferences.getDeviceId()?: "null") })
        )

        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                toDoItemDao.mergeToDoItems(responseBody.list.map { ToDoItemEntity.fromToDoTask(it.toToDoItem()) })
                sharedPreferences.putRevisionId(responseBody.revision)
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
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

}