package com.ctacek.yandexschool.doitnow.data.model

import java.io.Serializable
import java.util.Date

data class Todoitem (
    var id : String,
    var description: String,
    var priority : Priority,
    var endDate: Date?,
    var status : Boolean,
    val createdAt: Date,
    val lastUpdatedBy: Date?
        ): Serializable

enum class Priority {
    LOW, BASIC, HIGH
}