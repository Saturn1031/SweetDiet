package com.narae.sweetdiet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.narae.sweetdiet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    // 프래그먼트 어댑터
    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
        // 프래그먼트 리스트
        val fragments: List<Fragment>
        init {
            fragments = listOf(FragmentRecord(), FragmentAnalysis(), FragmentRecipe(), FragmentMore())
        }

        // 프래그먼트 개수 반환
        override fun getItemCount(): Int {
            return fragments.size
        }

        // 인덱스가 position인 프래그먼트 반환
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뷰페이저 어댑터 설정
        binding.viewpager.adapter = MyFragmentPagerAdapter(this)

        binding.viewpager.isUserInputEnabled = false

        // 탭 레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "식사 기록"
                1 -> tab.text = "식사 분석"
                2 -> tab.text = "레시피"
                3 -> tab.text = "더보기"
                }
            }.attach()

    }
}