package com.narae.sweetdiet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.narae.sweetdiet.MyApplication.Companion.auth

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val userImgToggle: SwitchPreferenceCompat? = findPreference("userProfile")

            // 사용자 프로필 사진 있으면 토글 enable
            val imageRef = MyApplication.storage.reference.child("images/${auth.currentUser?.email}.jpg")
            imageRef.metadata.addOnSuccessListener {
                userImgToggle?.isEnabled = true
            }
        }
    }
}