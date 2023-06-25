package com.ctacek.yandexschool.doitnow.data.datasource.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import java.util.Date

@Entity(tableName = "todoitems")
data class ToDoItemEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "importance") var importance: Importance,
    @ColumnInfo(name = "deadline") var deadline: Long?,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "changedAt") var changedAt: Long?
) {
    fun toToDoItem(): ToDoItem = ToDoItem(
        id,
        description,
        importance,
        deadline?.let { Date(it) },
        done,
        Date(createdAt),
        changedAt?.let { Date(it) }
    )
    companion object {
        fun fromToDoTask(toDoItem: ToDoItem): ToDoItemEntity {
            return ToDoItemEntity(
                id = toDoItem.id,
                description = toDoItem.description,
                importance = toDoItem.importance,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                createdAt = toDoItem.createdAt.time,
                changedAt = toDoItem.changedAt?.time
            )
        }
    }
}