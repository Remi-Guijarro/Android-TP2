package com.dm.todok.data

import com.dm.todok.network.Api
import com.dm.todok.model.Task

class TaskRepository {
    private val taskWebService = Api.INSTANCE.taskWebService

    suspend fun loadTasks(): List<Task>? {
        val response = taskWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task: Task) : Task? {
        val response = taskWebService.createTask(task)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteTask(task: Task) : Boolean {
        return task.id != null && taskWebService.deleteTask(task.id).isSuccessful
    }

    suspend fun updateTask(task: Task): Task? {
        val response = taskWebService.updateTask(task)
        return if (response.isSuccessful) response.body() else null
    }
}