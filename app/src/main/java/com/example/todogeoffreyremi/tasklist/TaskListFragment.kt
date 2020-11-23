package com.example.todogeoffreyremi.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todogeoffreyremi.R

class TaskListFragment : Fragment() {
    private val taskList = listOf("Task 1", "Task 2", "Task 3")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }
}