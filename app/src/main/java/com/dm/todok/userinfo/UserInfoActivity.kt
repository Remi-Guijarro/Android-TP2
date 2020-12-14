package com.dm.todok.userinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dm.todok.databinding.ActivityUserinfoBinding

class UserInfoActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUserinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}