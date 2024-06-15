package com.narae.sweetdiet

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.narae.sweetdiet.databinding.FragmentMoreBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMore.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMore : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: FragmentMoreBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)

        binding.userEmail.text = MyApplication.email

        binding.btnUserProfile.setOnClickListener {
            val intent = Intent(requireContext(), UserProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            Log.d("mobileapp", "로그 아웃")

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            // 현재 액티비티 종료
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val userStr = sharedPreferences.getString("userName", "User")
        binding.userName.text = userStr

        val userProfile = sharedPreferences.getBoolean("userProfile", false)
        // true 이면 사용자 사진 표시, false 이면 user_basic 표시
        if (userProfile) {
            val imageRef = MyApplication.storage.reference.child("images/${MyApplication.auth.currentUser?.email}.jpg")
            imageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(requireContext())
                        .load(task.result)
                        .into(binding.userImage)
                }
            }
        } else {
            binding.userImage.setImageResource(R.drawable.user_basic)
        }

        val color = sharedPreferences.getString("color", "#00000000")
        binding.userInfo.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))

        val size = sharedPreferences.getString("size", "12.0f")
        binding.userEmail.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size!!.toFloat())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMore.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMore().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}