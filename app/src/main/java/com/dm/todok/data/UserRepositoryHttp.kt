package com.dm.todok.data

import android.net.Uri
import androidx.core.net.toFile
import com.dm.todok.form.*
import com.dm.todok.model.UserInfo
import com.dm.todok.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject


class UserRepositoryHttp(private val api: Api) : UserRepository {

    private val userWebService = api.userWebService

    override suspend fun getInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun updateAvatar(url: Uri) : UserInfo? {
        val response = userWebService.updateAvatar(convert(url))
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun login(loginForm: LoginForm): LoginResponse? {
        val response = userWebService.login(loginForm)
        return if (response.isSuccessful) response.body() else null;
    }

    override suspend fun signUp(signUpForm: SignUpForm): Pair<SignUpResponse?, ErrorResponse?> {
        val response = userWebService.signUp(signUpForm)

        var signupResponse: SignUpResponse? = null
        val errorResponse: ErrorResponse? = ErrorResponse()
        if(response.isSuccessful)
            signupResponse = response.body()
        else {
            val jObjError = JSONObject(response.errorBody()?.charStream()!!.readText())
            val jObjErrorArray = jObjError.getJSONArray("errors")
            for (i in 0 until jObjErrorArray.length()) {
                val errorString = jObjErrorArray.getString(i)
                errorResponse!!.errors.add(errorString)
            }
        }

        return Pair(signupResponse, errorResponse)
    }

    private fun convert(uri: Uri) =
            MultipartBody
            .Part
            .createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = uri.toFile().asRequestBody()
            )
}