package com.ctacek.yandexschool.doitnow.data.datasource.remote

import android.util.Log
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.ToDoItemResponseRequest
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request.ToDoApiRequestElement
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.domain.model.DataState
import com.ctacek.yandexschool.doitnow.domain.model.ResponseState
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val service: ToDoItemService
) {

    private val TAG = RemoteDataSourceImpl::class.simpleName.toString()


    suspend fun getMergedList(currentList: List<ToDoItem>): Flow<DataState<List<ToDoItem>>> = flow {
        try {
            val networkListResponse = service.getList(token = sharedPreferences.getCurrentToken())

            if (networkListResponse.isSuccessful) {
                val body = networkListResponse.body()
                if (body != null) {
                    val revision = body.revision

                    val networkList = body.list.map { it.toToDoItem() }

                    val mergedMap = HashMap<String, ToDoItem>()

                    for (item in currentList) {
                        mergedMap[item.id] = item
                    }
                    for (item in networkList) {
                        if (mergedMap.containsKey(item.id)) {
                            val item1 = mergedMap[item.id]
                            if (item.changedAt!! > item1!!.changedAt) {
                                mergedMap[item.id] = item
                            } else {
                                mergedMap[item.id] = item1
                            }
                        } else if (revision != sharedPreferences.getRevisionId()) {
                            mergedMap[item.id] = item
                        }
                    }

                    val mergedList = mergedMap.values.toList().map {
                        ToDoItemResponseRequest.fromToDoTask(
                            it,
                            deviseId = sharedPreferences.getDeviceId()
                        )
                    }

                    sharedPreferences.putRevisionId(revision)

                    emitAll(updateRemoteTasks(mergedList))
                }
            } else {
                networkListResponse.errorBody()?.close()
            }

        } catch (e: Exception) {
            emit(DataState.Exception(e))
        }
    }

    private suspend fun updateRemoteTasks(mergedList: List<ToDoItemResponseRequest>): Flow<DataState<List<ToDoItem>>> =
        flow {
            try {
                val response = service.updateList(
                    lastKnownRevision = sharedPreferences.getRevisionId(),
                    token = sharedPreferences.getCurrentToken(),
                    body = ToDoApiRequestList(mergedList)
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        sharedPreferences.putRevisionId(responseBody.revision)
                        emit(DataState.Result(responseBody.list.map { it.toToDoItem() }))
                    }
                }
            } catch (e: Exception) {
                emit(DataState.Exception(e))
            }
        }


    suspend fun updateRemoteTask(toDoTask: ToDoItem) {
        try {
            val response = service.updateTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                itemId = toDoTask.id,
                body = ToDoApiRequestElement(
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
            Log.e(TAG, e.message.toString())
        }
    }

    suspend fun deleteRemoteTask(taskId: String) {
        try {
            val response = service.deleteTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
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
            Log.e(TAG, e.message.toString())
        }

    }

    suspend fun createRemoteTask(newTask: ToDoItem) {
        try {
            val response = service.addTask(
                lastKnownRevision = sharedPreferences.getRevisionId(),
                token = sharedPreferences.getCurrentToken(),
                newItem = ToDoApiRequestElement(
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
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }
}