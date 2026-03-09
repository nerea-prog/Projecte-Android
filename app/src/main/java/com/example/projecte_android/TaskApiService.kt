package com.example.projecte_android

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface TaskApiService {

    // Obtenir totes les tasques
    @GET("taskbuddy/api/task")
    suspend fun getAllTasks(): Response<List<MyItem>>

    // Obtenir una tasca per ID
    @GET("taskbuddy/api/task/{id}")
    suspend fun getTaskById(@Path("id") id: Long): Response<ResponseBody>

    // Crear tasques noves
    // En TaskApiService.kt
    @POST("taskbuddy/api/task")
    suspend fun createTasks(@Body tasks: List<MyItem>): Response<ResponseBody>

    // Actualitzar una tasca
    @PUT("taskbuddy/api/task/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: MyItem): Response<MyItem>

    // Eliminar una tasca per ID
    @DELETE("taskbuddy/api/task/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<ResponseBody>

    // Eliminar totes les tasques
    @DELETE("taskbuddy/api/tasks")
    suspend fun deleteAllTasks(): Response<ResponseBody>
}