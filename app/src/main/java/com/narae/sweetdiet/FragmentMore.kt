package com.narae.sweetdiet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.narae.sweetdiet.databinding.FragmentMoreBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLoginState

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
    var pushNotification = false

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

        binding.btnUserBodyInfo.setOnClickListener {
            val intent = Intent(requireContext(), UserBodyInfoEditActivity::class.java)
            startActivity(intent)
        }

        binding.btnUserProfile.setOnClickListener {
            val intent = Intent(requireContext(), UserProfileActivity::class.java)
            startActivity(intent)
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pushNotification = sharedPreferences.getBoolean("pushNotification", false)
        Log.d("mobileapp", "pushNotification : $pushNotification")

        // 알림 notification
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() ) {
            if (it.all { permission -> permission.value == true }) {
                noti()
            }
            else {
                Toast.makeText(requireContext(), "푸시 알림이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNotification.setOnClickListener {
            if (pushNotification) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            "android.permission.POST_NOTIFICATIONS"
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        noti()
                    } else {
                        permissionLauncher.launch(arrayOf("android.permission.POST_NOTIFICATIONS"))
                    }
                } else {
                    noti()
                }
            } else {
                Toast.makeText(requireContext(), "푸시 알림이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            MyApplication.auth.signOut()
            Log.d("mobileapp", "로그아웃")
            NaverIdLoginSDK.initialize(requireContext(), BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "sweetdiet")
            if (NaverIdLoginSDK.getState() == NidOAuthLoginState.OK) {
                Log.d("mobileapp", "네이버 로그아웃")
                NaverIdLoginSDK.logout()
            }
            MyApplication.email = null

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

        pushNotification = sharedPreferences.getBoolean("pushNotification", false)

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

    fun noti(){
        val manager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){     // 26 버전 이상
            val channelId="one-channel"
            val channelName="My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {   // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)  // 앱 런처 아이콘 상단에 숫자 배지를 표시할지 여부를 지정
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)
            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(requireContext(), channelId)
        }
        else {  // 26 버전 이하
            builder = NotificationCompat.Builder(requireContext())
        }

        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.drawable.logo)
            setWhen(System.currentTimeMillis())
            setContentTitle("당이어트 푸시 알림")
            setContentText("푸시 알림 받기 버튼을 클릭했습니다.")
        }

        manager.notify(11, builder.build())
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