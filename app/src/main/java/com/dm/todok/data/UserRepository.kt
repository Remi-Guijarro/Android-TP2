package com.dm.todok.data

import android.net.Uri
import com.dm.todok.form.*
import com.dm.todok.model.UserInfo

interface UserRepository {

    suspend fun getInfo(): UserInfo?
    suspend fun updateAvatar(url: Uri) : UserInfo?
    suspend fun login(loginForm: LoginForm): LoginResponse?
    suspend fun signUp(signUpForm: SignUpForm): Pair<SignUpResponse?, ErrorResponse?>

}