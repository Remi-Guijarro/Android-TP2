package com.dm.todok.usecases

import com.dm.todok.model.Task

interface TaskUseCases {
    suspend fun getAllTasks(): List<Task>?
    suspend fun getNumberOfTasks(limit: Int): List<Task>?
    suspend fun createTask(task: Task) : Task?
    suspend fun deleteTask(task: Task) : Boolean
    suspend fun updateTask(task: Task): Task?
}