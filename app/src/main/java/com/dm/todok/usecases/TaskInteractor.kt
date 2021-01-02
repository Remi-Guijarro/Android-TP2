package com.dm.todok.usecases

import com.dm.todok.data.TaskRepository
import com.dm.todok.model.Task

class TaskInteractor(private val taskRepository: TaskRepository) : TaskUseCases {
    override suspend fun getAllTasks(): List<Task>? =  taskRepository.loadTasks()


    override suspend fun getNumberOfTasks(limit: Int): List<Task>? {
        val allTasks = taskRepository.loadTasks()
        var tasks = mutableListOf<Task>()
        allTasks?.forEachIndexed {index, task ->if (index < limit) tasks.add(task)}
        return tasks
    }

    override suspend fun createTask(task: Task): Task? = taskRepository.createTask(task)

    override suspend fun deleteTask(task: Task): Boolean = taskRepository.deleteTask(task)

    override suspend fun updateTask(task: Task): Task? = taskRepository.updateTask(task)

}