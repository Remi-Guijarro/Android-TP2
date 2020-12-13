package com.dm.todok.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dm.todok.network.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {

    private val taskRepository = TaskRepository()

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    private fun getMutableList() = _taskList.value.orEmpty().toMutableList()

    fun loadTasks() {
        viewModelScope.launch {
            val tasks = taskRepository.loadTasks()
            _taskList.value = tasks.orEmpty()
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            taskRepository.createTask(task)?.let { task ->
                val mutableList = getMutableList()
                mutableList.add(task)
                _taskList.value = mutableList
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            if (taskRepository.deleteTask(task)) {
                val mutableList = getMutableList()
                mutableList.remove(task)
                _taskList.value = mutableList
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)?.let { task ->
                val mutableList = getMutableList()
                val position = mutableList.indexOfFirst { task.id == it.id }
                mutableList[position] = task
                _taskList.value = mutableList
            }
        }
    }
}