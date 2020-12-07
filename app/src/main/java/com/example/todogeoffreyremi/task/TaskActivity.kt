package com.example.todogeoffreyremi.task

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todogeoffreyremi.R
import com.example.todogeoffreyremi.tasklist.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val titleTextEdit = findViewById<EditText>(R.id.add_task_title)
        val descriptionTextEdit = findViewById<EditText>(R.id.add_task_description)

        val task : Task? = intent.getSerializableExtra(TASK_KEY) as? Task
        titleTextEdit.setText(task?.title)
        descriptionTextEdit.setText(task?.description)
        val id = task?.id ?: UUID.randomUUID().toString()

        val confirmButton = findViewById<FloatingActionButton>(R.id.add_task_confirm)
        confirmButton.setOnClickListener {
            val title = titleTextEdit.text.toString()
            val description = descriptionTextEdit.text.toString()

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

