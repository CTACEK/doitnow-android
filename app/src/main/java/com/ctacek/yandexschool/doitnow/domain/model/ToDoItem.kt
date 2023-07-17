package com.ctacek.yandexschool.doitnow.domain.model

import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.google.gson.Gson
import java.util.Date

/**
 * Data model for todoitems
 *
 * @author Yudov Stanislav
 *
 */
data class ToDoItem(
    var id: String,
    var description: String,
    var importance: Importance,
    var deadline: Date?,
    var done: Boolean,
    val createdAt: Date,
    var changedAt: Date?
) {
    constructor() : this("-1", "", Importance.BASIC, null, false, Date(), Date(System.currentTimeMillis()))

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
