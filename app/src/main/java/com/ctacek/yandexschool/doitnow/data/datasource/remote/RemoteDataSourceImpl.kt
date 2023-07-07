package com.ctacek.yandexschool.doitnow.data.datasource.remote

import android.util.Log
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.ToDoItemResponseRequest
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request.ToDoApiRequestElement
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.domain.model.DataState
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

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

                    sharedPreferences.putRevisionId(revision)

                    val mergedList = mergedMap.values.toList().map {
                        ToDoItemResponseRequest.fromToDoTask(
                            it,
                            deviseId = sharedPreferences.getDeviceId()
                        )
                    }

                    emitAll(updateRemoteTasks(mergedList))
                }
            } else {
                networkListResponse.errorBody()?.close()
            }

        } catch (exception: SocketTimeoutException) {
            emit(DataState.Exception(exception))
        } catch (exception: UnknownHostException) {
            emit(DataState.Exception(exception))
        } catch (exception: HttpException) {
            emit(DataState.Exception(exception))
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
            }  catch (exception: SocketTimeoutException) {
                emit(DataState.Exception(exception))
            } catch (exception: UnknownHostException) {
                emit(DataState.Exception(exception))
            } catch (exception: HttpException) {
                emit(DataState.Exception(exception))
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
        } catch (exception: SocketTimeoutException) {
            Log.d(TAG, exception.toString())
        } catch (exception: UnknownHostException){
            Log.d(TAG, exception.toString())
        } catch (exception: HttpException){
            Log.d(TAG, exception.toString())
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
        } catch (exception: SocketTimeoutException) {
            Log.d(TAG, exception.toString())
        } catch (exception: UnknownHostException){
            Log.d(TAG, exception.toString())
        } catch (exception: HttpException){
            Log.d(TAG, exception.toString())
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
        } catch (exception: SocketTimeoutException) {
            Log.d(TAG, exception.toString())
        } catch (exception: UnknownHostException){
            Log.d(TAG, exception.toString())
        } catch (exception: HttpException){
            Log.d(TAG, exception.toString())
        }
    }
}
