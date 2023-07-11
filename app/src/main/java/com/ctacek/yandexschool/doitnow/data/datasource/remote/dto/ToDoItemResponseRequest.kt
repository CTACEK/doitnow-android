package com.ctacek.yandexschool.doitnow.data.datasource.remote.dto

import com.ctacek.yandexschool.doitnow.utils.ImportanceSerializerAdapter
import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.domain.model.ToDoItem
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ToDoItemResponseRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("deadline")
    val deadline: Long?,

    @SerializedName("done")
    val done: Boolean,

    @SerializedName("color")
    val color: String?,

    @SerializedName("importance")
    @JsonAdapter(ImportanceSerializerAdapter::class)
    val importance: Importance,

    @SerializedName("created_at")
    val createdAt: Long,

    @SerializedName("changed_at")
    val changedAt: Long,

    @SerializedName("last_updated_by")
    val lastUpdatedBy: String,

    @SerializedName("text")
    val text: String
) {
    fun toToDoItem(): ToDoItem = ToDoItem(
        id,
        text,
        importance,
        deadline?.let { Date(it) },
        done,
        Date(createdAt),
        Date(changedAt)
    )
    companion object {
        fun fromToDoTask(toDoItem: ToDoItem, deviseId: String): ToDoItemResponseRequest {
            return ToDoItemResponseRequest(
                id = toDoItem.id,
                text = toDoItem.description,
                importance = toDoItem.importance,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                createdAt = toDoItem.createdAt.time,
                changedAt = toDoItem.changedAt?.time ?: 0,
                lastUpdatedBy = deviseId,
                color = null
            )
        }
    }
}
