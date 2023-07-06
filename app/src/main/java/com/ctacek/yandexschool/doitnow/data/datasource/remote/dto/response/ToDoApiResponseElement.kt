package com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.response

import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.ToDoItemResponseRequest
import com.google.gson.annotations.SerializedName

data class ToDoApiResponseElement(
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)