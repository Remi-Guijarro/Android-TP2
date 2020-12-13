package com.example.todogeoffreyremi.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todogeoffreyremi.R
import com.example.todogeoffreyremi.databinding.FragmentTaskListBinding
import com.example.todogeoffreyremi.network.Api
import com.example.todogeoffreyremi.task.TaskActivity
import com.example.todogeoffreyremi.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.example.todogeoffreyremi.task.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.example.todogeoffreyremi.task.TaskActivity.Companion.TASK_KEY
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    // View binding support in Fragment
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    val taskListAdapter: TaskListAdapter = TaskListAdapter()
    val layoutManager: LinearLayoutManager =  LinearLayoutManager(activity)

    // Todo (geoffrey): difference with val viewModel = TaskListViewModel()
    private val viewModel: TaskListViewModel by viewModels()

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
        viewModel.refreshTasks()
        taskListAdapter.notifyDataSetChanged()
        // Todo (geoffrey): Should we move this logic
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val userTextView = view?.findViewById<TextView>(R.id.user_text_view)
            userTextView?.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Todo (geoffrey): Bind to Fragment ?
        binding.taskListFragment = this

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        taskListAdapter.onDeleteTask = { task ->
            viewModel.deleteTask(task)
            taskListAdapter.notifyDataSetChanged()
        }

        taskListAdapter.onEditTask = { task ->
                val intent = Intent(activity, TaskActivity::class.java)
                intent.putExtra(TASK_KEY, task)
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        viewModel.taskList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
            viewModel.createTask(newTask)
            taskListAdapter.notifyDataSetChanged()
        }
        else if (requestCode == EDIT_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val updatedTask = data!!.getSerializableExtra(TASK_KEY) as Task
            viewModel.updateTask(updatedTask)
            taskListAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}