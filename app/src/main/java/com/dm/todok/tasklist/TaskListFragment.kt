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

        // Todo (geoffrey): Should we move this logic
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()
            binding.user = userInfo
        }

        binding.userAvatar.load("https://s1.qwant.com/thumbr/0x0/2/1/a5246dfdd4da59903560ad9218ed12dad6cdc771e9b88970afdf4c5c6ecf47/Nicolas-Sarkozy-@-France-24.jpg?u=https%3A%2F%2Fbeninwebtv.com%2Fwp-content%2Fuploads%2F2020%2F07%2FNicolas-Sarkozy-%40-France-24.jpg&q=0&b=1&p=0&a=1") {
            transformations(CircleCropTransformation())
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

        // Todo (geoffrey): 'this' error
/*        viewModel.taskList.observe(this, Observer { newList ->
            taskListAdapter.taskList = newList.toMutableList()
            taskListAdapter.notifyDataSetChanged()
        }))*/

        viewModel.taskList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { newList ->
            taskListAdapter.taskList = newList.toMutableList()
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