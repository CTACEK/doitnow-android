package com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request

import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.ToDoItemResponseRequest
import com.google.gson.annotations.SerializedName

data class ToDoApiRequestElement(
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)