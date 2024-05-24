package com.narae.sweetdiet

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.narae.sweetdiet.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    binding.email.text.clear()
                    binding.password.text.clear()
                    if(task.isSuccessful){
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener{sendTask ->
                                if(sendTask.isSuccessful){
                                    Toast.makeText(baseContext,"회원가입 성공! 이메일을 확인해주세요", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "회원가입 성공")
                                    finish()
                                }
                                else{
                                    Toast.makeText(baseContext,"이메일 발송 실패", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "이메일 발송 실패")
                                }
                            }
                    }
                    else{
                        Toast.makeText(baseContext,"회원가입 실패", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "== ${task.exception} ==")
                    }
                }
        }

        binding.login.setOnClickListener {
            finish()
        }
    }
}