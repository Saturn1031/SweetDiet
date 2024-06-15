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

        // 파일 읽기 (최초 실행 시에는 myBodyInfoFile.txt가 없으므로 주석처리 후 실행해야 함)
        val file = File(filesDir, "myBodyInfoFile.txt")
        val readstream : BufferedReader = file.reader().buffered()
        val bodyInfo = readstream.readLine()
        val height = bodyInfo.split("_")[0]
        val weight = bodyInfo.split("_")[1]
        val gender = bodyInfo.split("_")[2]

        binding.txtHeight.setText("키: " + height)
        binding.txtWeight.setText("체중: " + weight)
        binding.txtGender.setText("성별: " + gender)

        binding.btnSave.setOnClickListener {
            val height = binding.editHeight.text.toString()
            val weight = binding.editWeight.text.toString()
            var gender = ""

            if (binding.rgGender.checkedRadioButtonId == R.id.rbMale) {
                gender = "남자"
            } else {
                gender = "여자"
            }

            val file = File(filesDir, "myBodyInfoFile.txt")
            val writestream: OutputStreamWriter = file.writer()
            writestream.write(height + "_" + weight + "_" + gender)
            writestream.flush()

            finish()
        }
    }
}