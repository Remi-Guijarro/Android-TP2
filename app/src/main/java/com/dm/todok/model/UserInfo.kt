package com.dm.todok.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String,

    @SerialName("firstname")
    val firstName: String,

    @SerialName("lastname")
    val lastName: String,

    @SerialName("avatar")
    var avatar: String?
)
