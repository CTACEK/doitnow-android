package com.ctacek.yandexschool.doitnow.data.datasource.retrofit

import com.ctacek.yandexschool.doitnow.utils.Constants.TOKEN_API
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoItemApi {
    @GET("list")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun getList(): Response<ToDoApiResponse>

    @PATCH("list")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body list: List<ToDoItemResponse>
    ): Response<ToDoApiResponse>

    @GET("list/{id}")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun getListItemById(@Path("id") itemId: String): Response<ToDoApiResponse>

    @POST("list")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun addItemToList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body newItem: ToDoItemResponse
    ): Response<ToDoApiResponse>

    @PUT("list/{id}")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun changeListItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
        @Body updatedItem: ToDoItemResponse,
        D: Response<ToDoApiResponse>
    )

    @DELETE("list/{id}")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun deletelistItem(
        @Header("X-Last-Known-Revision") LastKnownRevision: Int,
        @Path("id") itemId: String,
    ): Response<ToDoApiResponse>
}