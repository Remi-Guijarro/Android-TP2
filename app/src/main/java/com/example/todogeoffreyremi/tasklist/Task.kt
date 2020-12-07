package com.example.todogeoffreyremi.tasklist

import java.io.Serializable

data class Task (
    val id : String? = null,
    var title : String? = "",
    var description : String? = "Task description",
) : Serializable