package com.narae.sweetdiet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.narae.sweetdiet.databinding.ActivityUserBodyInfoEditBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class UserBodyInfoEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBodyInfoEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBodyInfoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var height: String
        var weight: String
        var gender: String

        val file = File(filesDir, "myBodyInfoFile.txt")
        if (file.exists() == true) {
            val readstream: BufferedReader = file.reader().buffered()
            val bodyInfo = readstream.readLine()
            height = bodyInfo.split("_")[0]
            weight = bodyInfo.split("_")[1]
            gender = bodyInfo.split("_")[2]
        } else {
            height = "0"
            weight = "0"
            gender = "미설정"
        }

        if (height == "0") {
            binding.txtHeight.setText("키: " + "미설정")
        } else {
            binding.txtHeight.setText("키: " + height)
        }
        if (weight == "0") {
            binding.txtWeight.setText("체중: " + "미설정")
        } else {
            binding.txtWeight.setText("체중: " + weight)
        }
        binding.txtGender.setText("성별: " + gender)

        binding.btnSave.setOnClickListener {
            val editedHeight = binding.editHeight.text.toString()
            val editedWeight = binding.editWeight.text.toString()
            var editedGender = ""

            if (binding.rgGender.checkedRadioButtonId == R.id.rbMale) {
                editedGender = "남자"
            } else {
                editedGender = "여자"
            }

            val file = File(filesDir, "myBodyInfoFile.txt")
            val writestream: OutputStreamWriter = file.writer()
            writestream.write(editedHeight + "_" + editedWeight + "_" + editedGender)
            writestream.flush()

            finish()
        }
    }
}