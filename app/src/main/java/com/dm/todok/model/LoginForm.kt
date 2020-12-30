package com.dm.todok.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginForm (

    @SerialName("email")
    val email : String,

    @SerialName("password")
    val password :  String
) : java.io.Serializable