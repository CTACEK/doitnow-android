package com.ctacek.yandexschool.doitnow.data.datasource.remote

import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request.ToDoApiRequestElement
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.request.ToDoApiRequestList
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.response.ToDoApiResponseElement
import com.ctacek.yandexschool.doitnow.data.datasource.remote.dto.response.ToDoApiResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoItemService {
    @GET("list")
    suspend fun getList(@Header("Authorization") token: String): Response<ToDoApiResponseList>

    @PATCH("list")
    suspend fun updateList(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body body: ToDoApiRequestList
    ): Response<ToDoApiResponseList>

    @POST("list")
    suspend fun addTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body newItem: ToDoApiRequestElement
    ): Response<ToDoApiResponseElement>

    @PUT("list/{id}")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
        @Body body: ToDoApiRequestElement
    ): Response<ToDoApiResponseElement>

    @DELETE("list/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
    ): Response<ToDoApiResponseElement>
}
