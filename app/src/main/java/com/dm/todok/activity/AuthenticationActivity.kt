package com.dm.todok.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dm.todok.R
import com.dm.todok.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAuthenticationBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_authentication
        )
    }
}