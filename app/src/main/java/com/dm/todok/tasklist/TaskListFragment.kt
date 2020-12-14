package com.dm.todok.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
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
        viewModel.loadTasks()

        // Todo (geoffrey): move to user's view model
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()
            binding.user = userInfo
            binding.userAvatar.load(userInfo?.avatar) {
                transformations(CircleCropTransformation())
            }
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

        binding.userAvatar.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivityForResult(intent, 0)
        }

        taskListAdapter.onDeleteTask = { task ->
            viewModel.deleteTask(task)
        }

        taskListAdapter.onEditTask = { task ->
                val intent = Intent(activity, TaskActivity::class.java)
                intent.putExtra(TASK_KEY, task)
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
        }

        viewModel.taskList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { newList ->
            taskListAdapter.submitList(newList)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val newTask = data!!.getSerializableExtra(TASK_KEY) as Task
            viewModel.createTask(newTask)
        }
        else if (requestCode == EDIT_TASK_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val updatedTask = data!!.getSerializableExtra(TASK_KEY) as Task
            viewModel.updateTask(updatedTask)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}