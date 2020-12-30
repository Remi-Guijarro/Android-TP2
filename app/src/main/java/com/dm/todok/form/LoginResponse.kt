package com.dm.todok.form

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginResponse (
        @SerialName("token")
        val token : String
) : java.io.Serializable