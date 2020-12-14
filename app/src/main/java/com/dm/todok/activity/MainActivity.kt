package com.dm.todok.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dm.todok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // View binding support in Activities
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}