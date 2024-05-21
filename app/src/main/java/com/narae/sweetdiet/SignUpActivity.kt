package com.narae.sweetdiet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.narae.sweetdiet.databinding.ActivityLoginBinding
import com.narae.sweetdiet.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            finish()
        }
    }
}