package com.dm.todok

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dm.todok.tasklist.TaskListAdapter

@BindingAdapter("task_list_adapter")
fun setTaskListAdapter(recyclerView: RecyclerView, adapter: TaskListAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter("layout_manager")
fun setLayoutManager(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
    recyclerView.layoutManager = layoutManager
}
