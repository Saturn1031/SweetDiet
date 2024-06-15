package com.narae.sweetdiet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.narae.sweetdiet.MyApplication.Companion.auth
import com.narae.sweetdiet.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserProfileBinding
    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode === android.app.Activity.RESULT_OK) {
                binding.addImageView.visibility = View.VISIBLE
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .override(256, 256)
                    .into(binding.addImageView)
                uri = it.data?.data!!
            }
        }

        binding.uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        binding.saveButton.setOnClickListener {
            val userEmail = auth.currentUser?.email
            val imageRef = MyApplication.storage.reference.child("images/${userEmail}.jpg")

            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}