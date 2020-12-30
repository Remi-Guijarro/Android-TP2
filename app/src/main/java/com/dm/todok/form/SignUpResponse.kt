package com.dm.todok.form

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SignUpResponse (
        @SerialName("token")
        val token : String,
        @SerialName("errors")
        val errors: List<String> = listOf()
) : java.io.Serializable