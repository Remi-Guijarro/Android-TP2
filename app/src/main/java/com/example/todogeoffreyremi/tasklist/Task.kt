package com.example.todogeoffreyremi.tasklist

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Task (
    @SerialName("id")
    val id : String? = null,

    @SerialName("title")
    var title : String? = "",

    @SerialName("description")
    var description : String? = "Task description",
) : java.io.Serializable