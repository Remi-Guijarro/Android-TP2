package com.example.todogeoffreyremi.network

import com.example.todogeoffreyremi.tasklist.Task
import retrofit2.Response
import retrofit2.http.GET

interface TaskWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>
}