package com.example.todogeoffreyremi.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todogeoffreyremi.R
import com.example.todogeoffreyremi.databinding.FragmentTaskListBinding
import com.example.todogeoffreyremi.network.Api
import com.example.todogeoffreyremi.network.TaskRepository
import com.example.todogeoffreyremi.task.TaskActivity
import com.example.todogeoffreyremi.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.example.todogeoffreyremi.task.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.example.todogeoffreyremi.task.TaskActivity.Companion.TASK_KEY
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    // View binding support in Fragment
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3"))

    val taskListAdapter: TaskListAdapter = TaskListAdapter(taskList)
    val layoutManager: LinearLayoutManager =  LinearLayoutManager(activity)

    private val taskRepository = TaskRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val userTextView = view?.findViewById<TextView>(R.id.user_text_view)
            userTextView?.text = "${userInfo.firstName} ${userInfo.lastName}"
            taskRepository.refresh()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskListFragment = this

        val addButton = binding.addTaskButton
        addButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        taskListAdapter.onDeleteTask = { task ->
            lifecycleScope.launch {
                val success = taskRepository.deleteTask(task)
                if (success) {
                    taskListAdapter.notifyDataSetChanged()
                }
            }
        }

        taskListAdapter.onEditTask = { task ->
                val intent = Intent(activity, TaskActivity::class.java)
                intent.putExtra(TASK_KEY, task)
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        taskRepository.taskList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            taskListAdapter.taskList.clear()
            taskListAdapter.taskList.addAll(it)
            taskListAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val newTask = data!!.getSerializableExtra(TASK_KEY) as Task
            lifecycleScope.launch {
                taskRepository.createTask(newTask)
                taskListAdapter.notifyItemInserted(taskList.lastIndex)
            }
        }
        else if (requestCode == EDIT_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val updatedTask = data!!.getSerializableExtra(TASK_KEY) as Task
            lifecycleScope.launch {
                val position = taskRepository.updateTask(updatedTask)
                if (position != -1) {
                    taskListAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}