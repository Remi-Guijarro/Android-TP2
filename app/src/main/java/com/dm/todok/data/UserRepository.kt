package com.dm.todok.data

import android.net.Uri
import androidx.core.net.toFile
import com.dm.todok.network.Api
import com.dm.todok.model.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UserRepository {

    private val userWebService = Api.userWebService

    suspend fun getInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateAvatar(url: Uri) : UserInfo? {
        val response = userWebService.updateAvatar(convert(url))
        return if (response.isSuccessful) response.body() else null
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