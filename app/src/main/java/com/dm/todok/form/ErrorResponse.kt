package com.dm.todok.form

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ErrorResponse (
        @SerialName("errors")
        var errors: MutableList<String> = mutableListOf()
) : java.io.Serializable