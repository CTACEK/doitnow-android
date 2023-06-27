package com.ctacek.yandexschool.doitnow.data.datasource.retrofit

import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.ctacek.yandexschool.doitnow.data.model.ToDoItem
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ToDoApiResponseList(
    @SerializedName("status")
    val status: String,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)

data class ToDoApiRequestList(
    @SerializedName("status")
    val status: String,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)

data class ToDoApiResponseElement(
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)

data class ToDoApiRequestElement(
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)

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
    @JsonAdapter(ImportanceAdapter::class)
    val importance: Importance,

    @SerializedName("created_at")
    val created_at: Long,

    @SerializedName("changed_at")
    val changed_at: Long,

    @SerializedName("last_updated_by")
    val last_updated_by: String,

    @SerializedName("text")
    val text: String
) {
    fun toToDoItem(): ToDoItem = ToDoItem(
        id,
        text,
        importance,
        deadline?.let { Date(it) },
        done,
        Date(created_at),
        Date(changed_at)
    )
    companion object {
        fun fromToDoTask(toDoItem: ToDoItem): ToDoItemResponseRequest {
            return ToDoItemResponseRequest(
                id = toDoItem.id,
                text = toDoItem.description,
                importance = toDoItem.importance,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                created_at = toDoItem.createdAt.time,
                changed_at = toDoItem.changedAt?.time ?: 0,
                last_updated_by = "d1",
                color = null
            )
        }
    }
}