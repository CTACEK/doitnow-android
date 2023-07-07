package com.ctacek.yandexschool.doitnow.data.repository

import android.util.Log
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDao
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.datasource.remote.RemoteDataSourceImpl
import com.ctacek.yandexschool.doitnow.domain.model.DataState
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import com.ctacek.yandexschool.doitnow.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Repository for todoitems
 *
 * @author Yudov Stanislav
 *
 */
class ToDoItemsRepositoryImpl @Inject constructor(
    private val dao: ToDoItemDao,
    private val networkSource: RemoteDataSourceImpl
) : Repository {

    private val TAG = ToDoItemsRepositoryImpl::class.simpleName

    override fun getAllData(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        dao.getToDoItems().collect { list ->
            emit(UiState.Success(list.map { it.toToDoItem() }))
        }
    }

    override suspend fun addItem(todoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(todoItem)
        dao.createItem(toDoItemEntity)

        networkSource.createRemoteTask(todoItem)
    }

    override suspend fun deleteItem(todoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(todoItem)
        dao.deleteToDoItem(toDoItemEntity)
        try {
            networkSource.deleteRemoteTask(todoItem.id)
        } catch (exception: SocketTimeoutException) {
            Log.d(TAG, exception.toString())
        } catch (exception: UnknownHostException){
            Log.d(TAG, exception.toString())
        } catch (exception: HttpException){
            Log.d(TAG, exception.toString())
        }
    }

    override suspend fun changeItem(todoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(todoItem)
        dao.updateToDoItem(toDoItemEntity)
        try {
            networkSource.updateRemoteTask(todoItem)
        } catch (exception: SocketTimeoutException) {
            Log.d(TAG, exception.toString())
        } catch (exception: UnknownHostException){
            Log.d(TAG, exception.toString())
        } catch (exception: HttpException){
            Log.d(TAG, exception.toString())
        }
    }

    override fun getNetworkTasks(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        networkSource.getMergedList(
            dao.getToDoItemsNoFlow().map { it.toToDoItem() })
            .collect { state ->
                when (state) {
                    DataState.Initial -> emit(UiState.Start)
                    is DataState.Exception -> emit(UiState.Error(state.cause.message.toString()))
                    is DataState.Result -> {
                        dao.mergeToDoItems(state.data.map { ToDoItemEntity.fromToDoTask(it) })
                        emit(UiState.Success(state.data))
                    }
                }
            }
    }

    override fun getItem(itemId: String): ToDoItem = dao.getToDoItemById(itemId).toToDoItem()

    override suspend fun deleteCurrentItems() {
        dao.deleteAllToDoItems()
    }
}
