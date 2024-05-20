package com.narae.sweetdiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.narae.sweetdiet.databinding.ActivityLoginBinding
import com.narae.sweetdiet.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
    }
}