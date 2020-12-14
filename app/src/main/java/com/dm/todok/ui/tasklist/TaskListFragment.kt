package com.dm.todok.ui.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dm.todok.ui.user.UserViewModel
import com.dm.todok.databinding.FragmentTaskListBinding
import com.dm.todok.model.Task
import com.dm.todok.activity.TaskActivity
import com.dm.todok.activity.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.dm.todok.activity.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.dm.todok.activity.TaskActivity.Companion.TASK_KEY
import com.dm.todok.activity.UserInfoActivity

class TaskListFragment : Fragment() {
    // View binding support in Fragment
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val taskListAdapter: TaskListAdapter = TaskListAdapter()

    private val taskListViewModel: TaskListViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

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
        refreshModels()
    }

    private fun refreshModels() {
        taskListViewModel.refreshTasks()
        userViewModel.refreshUserInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = taskListAdapter
        binding.lifecycleOwner = this

        bindModels()

        setClickListeners()
        setAdapterCallbacks()

        taskListViewModel.taskList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { newList ->
            taskListAdapter.submitList(newList)
        })
    }

    private fun bindModels() {
        userViewModel.userInfo.observe(viewLifecycleOwner, {
            binding.executePendingBindings()
            binding.userViewModel = userViewModel
        })
        binding.userViewModel = userViewModel
        binding.taskListViewModel = taskListViewModel
    }

    private fun setClickListeners() {
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        binding.userAvatar.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    private fun setAdapterCallbacks() {
        taskListAdapter.onDeleteTask = { task ->
            taskListViewModel.deleteTask(task)
        }

        taskListAdapter.onEditTask = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TASK_KEY, task)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val newTask = data!!.getSerializableExtra(TASK_KEY) as Task
            taskListViewModel.createTask(newTask)
        }
        else if (requestCode == EDIT_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val updatedTask = data!!.getSerializableExtra(TASK_KEY) as Task
            taskListViewModel.updateTask(updatedTask)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}