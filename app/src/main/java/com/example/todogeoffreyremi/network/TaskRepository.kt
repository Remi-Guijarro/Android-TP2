package com.example.todogeoffreyremi.network

import com.example.todogeoffreyremi.tasklist.Task

class TaskRepository {
    private val taskWebService = Api.taskWebService

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