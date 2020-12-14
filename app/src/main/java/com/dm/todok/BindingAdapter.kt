package com.dm.todok

import androidx.databinding.BindingAdapter
import androidx.fragment.app.findFragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.dm.todok.tasklist.Task
import com.dm.todok.tasklist.TaskListAdapter
import com.dm.todok.tasklist.TaskListFragment

@BindingAdapter("app:observe_list")
fun observeList(recyclerView: RecyclerView, taskList: LiveData<List<Task>>) {
    taskList.observe(recyclerView.findFragment<TaskListFragment>().viewLifecycleOwner, androidx.lifecycle.Observer { newList ->
        val adapter: TaskListAdapter = recyclerView.adapter as TaskListAdapter
        adapter.submitList(newList)
    })
}
