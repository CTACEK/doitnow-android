package com.ctacek.yandexschool.doitnow.data.repository

import android.util.Log
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestElement
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemApi
import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.ToDoItemResponseRequest
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.room.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.model.LoadingState
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
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
    private val LOG_TAG = ToDoItemsRepository::class.simpleName.toString()

    fun getAllToDoItems(): Flow<List<ToDoItem>> {
        return toDoItemDao.getToDoItems().map { it -> it.map { it.toToDoItem() } }
    }

    fun getToDoItemById(id: String): ToDoItem {
        return toDoItemDao.getToDoItemById(id = id).toToDoItem()
    }

    suspend fun updateStatusToDoItem(id : String, done: Boolean) {
        return toDoItemDao.updateDone(id, done, System.currentTimeMillis())
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

    suspend fun deleteAll() {
        toDoItemDao.deleteAllToDoItems()
    }

    suspend fun updateRemoteTask(toDoTask: ToDoItem) {
        try {
            val response = remoteDataSource.updateTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                itemId = toDoTask.id,
                ToDoApiRequestElement(
                    ToDoItemResponseRequest.fromToDoTask(
                        toDoTask,
                        sharedPreferences.getDeviceId()
                    )
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
        }


    }

    suspend fun deleteRemoteTask(taskId: String) {
        try {
            val response = remoteDataSource.deleteTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                itemId = taskId
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }

    }

    suspend fun createRemoteTask(newTask: ToDoItem) {
        try {
            val response = remoteDataSource.addTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                ToDoApiRequestElement(
                    ToDoItemResponseRequest.fromToDoTask(
                        newTask,
                        sharedPreferences.getDeviceId()
                    )
                )
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.toString())
        }
    }

    private suspend fun updateRemoteTasks(mergedList: List<ToDoItemResponseRequest>): LoadingState<Any> {
        try {
            val response = remoteDataSource.updateList(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                ToDoApiRequestList(status = "ok", mergedList)
            )

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    sharedPreferences.putRevisionId(responseBody.revision)
                    toDoItemDao.mergeToDoItems(responseBody.list.map {
                        ToDoItemEntity.fromToDoTask(it.toToDoItem())
                    })
                    return LoadingState.Success(responseBody.list)
                }
            } else {
                response.errorBody()?.close()
            }
        } catch (e: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }

        return LoadingState.Error("Merge failed, continue offline.")
    }

    suspend fun getRemoteTasks(): LoadingState<Any> {
        try {
            val networkListResponse = remoteDataSource.getList()

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision
                    val networkList = body.list
                    val currentList = toDoItemDao.getToDoItemsNoFlow().map {
                        ToDoItemResponseRequest.fromToDoTask(
                            it.toToDoItem(),
                            sharedPreferences.getDeviceId()
                        )
                    }
                    val mergedList = HashMap<String, ToDoItemResponseRequest>()

                    for (item in currentList) {
                        mergedList[item.id] = item
                    }
                    for (item in networkList) {
                        if (mergedList.containsKey(item.id)) {
                            val item1 = mergedList[item.id]
                            if (item.changed_at > item1!!.changed_at) {
                                mergedList[item.id] = item
                            } else {
                                mergedList[item.id] = item1
                            }
                        } else if (revision != sharedPreferences.getRevisionId()) {
                            mergedList[item.id] = item
                        }
                    }

                    return updateRemoteTasks(mergedList.values.toList())
                }
            } else {
                networkListResponse.errorBody()?.close()
            }

        } catch (e: Exception) {
            return LoadingState.Error("Merge failed, continue offline.")
        }
        return LoadingState.Error("Merge failed, continue offline.")
    }

}