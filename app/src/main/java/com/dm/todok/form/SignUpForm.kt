package com.dm.todok.form

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SignUpForm(
        @SerialName("firstname")
        val name : String,

        @SerialName("lastname")
        val lastName :  String,

        @SerialName("email")
        val email : String,

        @SerialName("password")
        val password :  String,

        @SerialName("password_confirmation")
        val password_confirmation :  String,
        ) : java.io.Serializable
