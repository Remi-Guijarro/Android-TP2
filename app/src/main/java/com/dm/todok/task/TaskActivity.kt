package com.dm.todok.task

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dm.todok.R
import com.dm.todok.databinding.ActivityTaskBinding
import com.dm.todok.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTaskBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_task
        )

        val task : Task? = intent.getSerializableExtra(TASK_KEY) as? Task
        binding.task = task

        val id = task?.id ?: UUID.randomUUID().toString()

        binding.addTaskConfirmButton.setOnClickListener {
            val title = binding.addTaskTitle.text.toString()
            val description = binding.addTaskDescription.text.toString()

            val newTask = Task(id = id, title = title, description = description)
            val intent = Intent()
            intent.putExtra(TASK_KEY, newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val TASK_KEY = "newTask"
        const val ADD_TASK_REQUEST_CODE = 666
        const val EDIT_TASK_REQUEST_CODE = 777
    }
}

