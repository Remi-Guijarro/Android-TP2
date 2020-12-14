package com.dm.todok

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dm.todok.network.UserInfo
import com.dm.todok.network.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _userInfo = MutableLiveData<UserInfo>()

    val userInfo: LiveData<UserInfo> = _userInfo

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            _userInfo.value = userRepository.getInfo()
        }
    }

    fun refreshUserInfo() {
        loadUserInfo()
    }

    fun updateAvatar(url: Uri) {
        viewModelScope.launch {
            userRepository.updateAvatar(url)
        }
    }
}