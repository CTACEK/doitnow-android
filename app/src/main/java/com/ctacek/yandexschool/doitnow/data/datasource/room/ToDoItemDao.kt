package com.ctacek.yandexschool.doitnow.data.datasource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {

    @Query("SELECT * FROM todoitems ORDER BY id")
    fun getToDoItems(): Flow<List<ToDoItemEntity>>

    @Query("SELECT * FROM todoitems WHERE done == 0")
    fun getNotCompletedToDoItems(): Flow<List<ToDoItemEntity>>

    @Query("SELECT * FROM todoitems WHERE id = :id")
    fun getToDoItemById(id: String): Flow<ToDoItemEntity>

    @Update
    suspend fun updateToDoItem(toDoItemEntity: ToDoItemEntity)

    @Query("UPDATE todoitems SET done=:done WHERE id=:id")
    suspend fun updateDone(id: String, done: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createItem(vararg itemEntity: ToDoItemEntity)

    @Delete
    suspend fun deleteToDoItem(toDoItemEntity: ToDoItemEntity)

}