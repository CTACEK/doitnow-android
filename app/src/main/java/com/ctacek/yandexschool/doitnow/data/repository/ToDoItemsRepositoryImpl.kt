package com.ctacek.yandexschool.doitnow.data.repository

import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDao
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemEntity
import com.ctacek.yandexschool.doitnow.data.datasource.remote.RemoteDataSourceImpl
import com.ctacek.yandexschool.doitnow.domain.model.DataState
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.ctacek.yandexschool.doitnow.domain.model.UiState
import com.ctacek.yandexschool.doitnow.domain.repository.Repository
import com.ctacek.yandexschool.doitnow.utils.notificationmanager.NotificationScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository for todoitems
 *
 * @author Yudov Stanislav
 *
 */
class ToDoItemsRepositoryImpl @Inject constructor(
    private val dao: ToDoItemDao,
    private val networkSource: RemoteDataSourceImpl,
    private val notificationsScheduler: NotificationScheduler
) : Repository {

    override fun getAllData(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        dao.getToDoItems().collect { list ->
            emit(UiState.Success(list.map { it.toToDoItem() }))
        }
    }

    override suspend fun addItem(todoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(todoItem)
        dao.createItem(toDoItemEntity)

        notificationsScheduler.schedule(todoItem)
        networkSource.createRemoteTask(todoItem)
    }

    override suspend fun deleteItem(todoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(todoItem)
        dao.deleteToDoItem(toDoItemEntity)

        notificationsScheduler.cancel(todoItem)
        networkSource.deleteRemoteTask(todoItem.id)
    }

    override suspend fun changeItem(todoItem: ToDoItem) {
        val toDoItemEntity = ToDoItemEntity.fromToDoTask(todoItem)
        dao.updateToDoItem(toDoItemEntity)

        notificationsScheduler.schedule(todoItem)
        networkSource.updateRemoteTask(todoItem)
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
                        updateNotifications(state.data)
                        emit(UiState.Success(state.data))
                    }
                }
            }
    }

    private fun updateNotifications(items: List<ToDoItem>) {
        notificationsScheduler.cancelAll()
        for (item in items){
            notificationsScheduler.schedule(item)
        }
    }

    override fun getItem(itemId: String): ToDoItem = dao.getToDoItemById(itemId).toToDoItem()

    override suspend fun deleteCurrentLocalItems() {
        dao.deleteAllToDoItems()
    }
}
