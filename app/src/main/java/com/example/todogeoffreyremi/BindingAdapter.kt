package com.example.todogeoffreyremi

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todogeoffreyremi.tasklist.TaskListAdapter

@BindingAdapter("task_list_adapter")
fun setTaskListAdapter(recyclerView: RecyclerView, adapter: TaskListAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter("layout_manager")
fun setLayoutManager(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
    recyclerView.layoutManager = layoutManager
}
