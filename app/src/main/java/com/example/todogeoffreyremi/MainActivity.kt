package com.example.todogeoffreyremi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todogeoffreyremi.databinding.ActivityMainBinding

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