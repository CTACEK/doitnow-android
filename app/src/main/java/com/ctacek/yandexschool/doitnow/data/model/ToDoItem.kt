package com.ctacek.yandexschool.doitnow.data.model

import java.io.Serializable
import java.util.Date

data class ToDoItem(
    var id: String,
    var description: String,
    var priority: Priority,
    var endDate: Date?,
    var status: Boolean,
    val createdAt: Date,
    var lastUpdatedBy: Date?
) : Serializable {
    constructor() : this("999", "", Priority.BASIC, null, false, Date(), Date())
}

enum class Priority {
    LOW, BASIC, HIGH
}