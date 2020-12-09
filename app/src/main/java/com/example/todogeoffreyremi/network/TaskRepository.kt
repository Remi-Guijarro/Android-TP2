package com.example.todogeoffreyremi.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todogeoffreyremi.tasklist.Task

class TaskRepository {
    private val taskWebService = Api.taskWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = taskWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun deleteTask(task: Task) : Boolean {
        val response = taskWebService.deleteTask(task.id)
        if (response.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList.removeAt(position)
            _taskList.value = editableList
        }
        return response.isSuccessful
    }

    suspend fun updateTask(task: Task): Int {
        val response = taskWebService.updateTask(task)
        var position = -1
        if (response.isSuccessful) {
            val updatedTask = response.body()
            val editableList = _taskList.value.orEmpty().toMutableList()
            position = editableList.indexOfFirst { updatedTask!!.id == it.id }
            editableList[position] = updatedTask!!
            _taskList.value = editableList
        }
        return position
    }
}