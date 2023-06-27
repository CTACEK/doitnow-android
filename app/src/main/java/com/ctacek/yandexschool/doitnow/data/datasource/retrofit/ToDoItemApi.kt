package com.ctacek.yandexschool.doitnow.data.datasource.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoItemApi {
    @GET("list")
    suspend fun getList(): Response<ToDoApiResponseList>

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body body: ToDoApiRequestList
    ): Response<ToDoApiResponseList>

    @GET("list/{id}")
    suspend fun getListItemById(@Path("id") itemId: String): Response<ToDoApiResponseElement>

    @POST("list")
    suspend fun addItemToList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body newItem: ToDoApiResponseElement
    ): Response<ToDoApiResponseElement>

    @PUT("list/{id}")
    suspend fun changeListItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
        @Body updatedItem: ToDoItemResponseRequest,
        D: Response<ToDoApiResponseElement>
    )

    @DELETE("list/{id}")
    suspend fun deletelistItem(
        @Header("X-Last-Known-Revision") LastKnownRevision: Int,
        @Path("id") itemId: String,
    ): Response<ToDoApiResponseElement>
}