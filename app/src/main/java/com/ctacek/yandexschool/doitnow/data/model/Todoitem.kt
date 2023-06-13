package com.ctacek.yandexschool.doitnow.data.model

import java.io.Serializable
import java.util.Date

data class Todoitem (
    val id : String,
    val description: String,
    val priority : Priority,
    val endDate: Date?,
    var status : Boolean,
    val createdAt: Date,
    val lastUpdatedBy: Date?
        ): Serializable

enum class Priority {
    LOW, BASIC, HIGH
}