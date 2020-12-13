package com.example.todogeoffreyremi.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todogeoffreyremi.R
import com.example.todogeoffreyremi.databinding.ItemTaskBinding

class TaskListAdapter(val taskList: MutableList<Task> = mutableListOf()) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback) {

    // Todo (geoffrey): Why is this never called ?
    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    }

    var onDeleteTask: ((Task) -> Unit)? = null
    var onEditTask: ((Task) -> Unit)? = null

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Support binding in view holder
        private val binding = ItemTaskBinding.bind(itemView)

        fun bind(task: Task) {
            binding.task = task

            binding.taskDeleteButton.setOnClickListener {
                onDeleteTask?.invoke(task)
            }

            binding.taskEditButton.setOnClickListener {
                onEditTask?.invoke(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

}