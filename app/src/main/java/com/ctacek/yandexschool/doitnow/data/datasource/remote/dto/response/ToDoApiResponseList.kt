package com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.response

import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.ToDoItemResponseRequest
import com.google.gson.annotations.SerializedName

data class ToDoApiResponseList(
    @SerializedName("status")
    val status: String,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)
