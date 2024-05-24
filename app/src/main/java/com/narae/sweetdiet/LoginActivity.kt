package com.narae.sweetdiet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.narae.sweetdiet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {   // 로그인 Button
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    binding.email.text.clear()
                    binding.password.text.clear()
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            Log.d("mobileapp", "로그인 성공")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(baseContext,"이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "이메일 인증 안됨")
                        }
                    }
                    else{
                        Toast.makeText(baseContext,"로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "로그인 실패")
                    }
                }
        }

        binding.signUp.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Log.d("mobileapp","account1 : ${task.toString()}")
            //Log.d("mobileapp","account2 : ${task.result}")
            try{
                val account = task.getResult(ApiException::class.java)
                val crendential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(crendential)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email
                            Toast.makeText(baseContext,"구글 로그인 성공", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 성공")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(baseContext,"구글 로그인 실패", Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 실패")
                        }
                    }
            }catch (e: ApiException){ // APIException은 이미 지정된 exception말고 custom한 exception을 만들어서 쓰고 싶을때 사용
                Toast.makeText(baseContext,"구글 로그인 Exception : ${e.printStackTrace()},${e.statusCode}",
                    Toast.LENGTH_SHORT).show()
                Log.d("mobileapp", "구글 로그인 Exception : ${e.message}, ${e.statusCode}")
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent
            requestLauncher.launch(signInIntent)
        }
    }
}