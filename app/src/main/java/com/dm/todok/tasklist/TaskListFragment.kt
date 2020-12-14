package com.dm.todok.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.dm.todok.UserViewModel
import com.dm.todok.databinding.FragmentTaskListBinding
import com.dm.todok.network.Api
import com.dm.todok.task.TaskActivity
import com.dm.todok.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.dm.todok.task.TaskActivity.Companion.EDIT_TASK_REQUEST_CODE
import com.dm.todok.task.TaskActivity.Companion.TASK_KEY
import com.dm.todok.userinfo.UserInfoActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    // View binding support in Fragment
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val taskListAdapter: TaskListAdapter = TaskListAdapter()

    // Todo (geoffrey): difference with val viewModel = TaskListViewModel()
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

        bindModels()
        binding.recyclerView.adapter = taskListAdapter

        setClickListeners()
        setAdapterCallbacks()

        taskListViewModel.taskList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { newList ->
            taskListAdapter.submitList(newList)
        })
    }

    private fun bindModels() {
        userViewModel.userInfo.observe(viewLifecycleOwner, {
            binding.userViewModel = userViewModel
        })
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