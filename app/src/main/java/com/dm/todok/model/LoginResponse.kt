package com.dm.todok.model

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class LoginResponse (
        @SerialName("token")
        val token : String
) : java.io.Serializable